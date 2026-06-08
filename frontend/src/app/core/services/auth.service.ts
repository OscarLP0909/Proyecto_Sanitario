import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest, JwtPayload, UserRole } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly API = '/api';

  private _token = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));
  private _fullName = signal<string | null>(null);

  readonly isAuthenticated = computed(() => !!this._token());
  readonly currentUser = computed(() => {
    const token = this._token();
    if (!token) return null;
    return this.decodeToken(token);
  });
  readonly userRole = computed<UserRole | null>(() => {
    const user = this.currentUser();
    if (!user) return null;
    // Strip ROLE_ prefix if backend sends ROLE_ADMIN, ROLE_MEDICO, ROLE_PACIENTE
    const raw = user.role ?? (user as any)['authorities']?.[0]?.authority ?? null;
    if (!raw) return null;
    return raw.replace(/^ROLE_/, '') as UserRole;
  });
  readonly userFullName = computed(() => {
    const fullName = this._fullName();
    if (fullName) return fullName;
    const user = this.currentUser();
    return user?.sub ?? '';
  });

  constructor(private http: HttpClient, private router: Router) {
    if (this._token()) {
      this.loadFullName();
    }
  }

  login(req: LoginRequest) {
    return this.http.post<LoginResponse>(`${this.API}/auth/login`, req).pipe(
      tap(res => this.saveTokens(res.token, res.refreshToken))
    );
  }

  register(req: RegisterRequest) {
    return this.http.post<void>(`${this.API}/auth/register`, req);
  }

  refreshToken() {
    const refreshToken = this.getRefreshToken();
    return this.http.post<LoginResponse>(`${this.API}/auth/refresh`, { refreshToken }).pipe(
      tap(res => this.saveTokens(res.token, res.refreshToken))
    );
  }

  logout() {
    this.http.post<void>(`${this.API}/auth/logout`, {}).pipe(
      catchError(() => of(void 0))
    ).subscribe(() => this.clearSession());
  }

  clearSession() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    this._token.set(null);
    this._fullName.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this._token();
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  private saveTokens(token: string, refreshToken: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
    this._token.set(token);
    this.loadFullName();
  }

  private loadFullName() {
    const role = this.userRole();
    const endpoint = role === 'MEDICO' ? 'medicos' : role === 'PACIENTE' ? 'pacientes' : null;
    if (!endpoint) return;

    this.http.get<{ name: string; surname: string }>(`${this.API}/${endpoint}/me`).pipe(
      catchError(() => of(null))
    ).subscribe(res => {
      if (res) this._fullName.set(`${res.name} ${res.surname}`);
    });
  }

  private decodeToken(token: string): JwtPayload | null {
    try {
      const parts = token.split('.');
      if (parts.length !== 3) return null;
      const payload = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      const padded = payload + '='.repeat((4 - payload.length % 4) % 4);
      return JSON.parse(atob(padded));
    } catch {
      return null;
    }
  }
}
