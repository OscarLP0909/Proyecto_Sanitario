import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse, RegisterRequest, JwtPayload, UserRole } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly API = '/api';

  private _token = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));

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
    const user = this.currentUser();
    if (!user) return '';
    // JWT only has sub (email) — display email as username
    return user.sub ?? '';
  });

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest) {
    return this.http.post<LoginResponse>(`${this.API}/auth/login`, req).pipe(
      tap(res => this.saveToken(res.token))
    );
  }

  register(req: RegisterRequest) {
    return this.http.post<void>(`${this.API}/auth/register`, req);
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    this._token.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this._token();
  }

  private saveToken(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
    this._token.set(token);
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
