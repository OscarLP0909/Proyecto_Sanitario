import { HttpErrorResponse, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

const isRefreshing = new BehaviorSubject<boolean>(false);
const refreshedToken = new BehaviorSubject<string | null>(null);

const isPublicUrl = (url: string) =>
  url.includes('/auth/login') ||
  url.includes('/auth/register') ||
  url.includes('/auth/refresh');

const withAuth = (req: HttpRequest<unknown>, token: string) =>
  req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();
  const isPublic = isPublicUrl(req.url);

  const authReq = token && !isPublic ? withAuth(req, token) : req;

  return next(authReq).pipe(
    catchError((error: unknown) => {
      if (
        error instanceof HttpErrorResponse &&
        error.status === 401 &&
        !isPublic &&
        auth.getRefreshToken()
      ) {
        return handle401(req, next, auth);
      }
      return throwError(() => error);
    })
  );
};

function handle401(req: HttpRequest<unknown>, next: Parameters<HttpInterceptorFn>[1], auth: AuthService) {
  if (!isRefreshing.value) {
    isRefreshing.next(true);
    refreshedToken.next(null);

    return auth.refreshToken().pipe(
      switchMap(res => {
        isRefreshing.next(false);
        refreshedToken.next(res.token);
        return next(withAuth(req, res.token));
      }),
      catchError(err => {
        isRefreshing.next(false);
        auth.clearSession();
        return throwError(() => err);
      })
    );
  }

  return refreshedToken.pipe(
    filter((token): token is string => token !== null),
    take(1),
    switchMap(token => next(withAuth(req, token)))
  );
}
