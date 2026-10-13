import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

// État partagé entre toutes les requêtes concurrentes
let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const refreshInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Ne pas retenter pour les endpoints d'auth (évite la boucle infinie)
      const isAuthEndpoint = req.url.includes('/api/auth/');
      if (error.status !== 401 || isAuthEndpoint) {
        return throwError(() => error);
      }

      // Cas 1 : un refresh est déjà en cours → on attend le nouveau token
      if (isRefreshing) {
        return refreshTokenSubject.pipe(
          filter(token => token !== null),  // ignore les valeurs intermédiaires
          take(1),                           // prend la première valeur non-null
          switchMap(token => {
            const cloned = req.clone({
              setHeaders: { Authorization: `Bearer ${token}` }
            });
            return next(cloned);
          })
        );
      }

      // Cas 2 : c'est nous qui déclenchons le refresh
      isRefreshing = true;
      refreshTokenSubject.next(null);  // signal "en cours" aux autres

      return auth.refresh().pipe(
        switchMap(res => {
          isRefreshing = false;
          refreshTokenSubject.next(res.accessToken);  // libère les autres
          const cloned = req.clone({
            setHeaders: { Authorization: `Bearer ${res.accessToken}` }
          });
          return next(cloned);
        }),
        catchError(refreshError => {
          // Le refresh a échoué → session terminée, déconnexion
          isRefreshing = false;
          refreshTokenSubject.next(null);
          auth.clearSession();
          return throwError(() => refreshError);
        })
      );
    })
  );
};
