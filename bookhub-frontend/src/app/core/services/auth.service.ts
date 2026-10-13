import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  username: string;
  type: string;
}

export interface RefreshResponse {
  accessToken: string;
  refreshToken: string;
}

const TOKEN_KEY = 'bookhub.token';
const REFRESH_KEY = 'bookhub.refreshToken';
const USERNAME_KEY = 'bookhub.username';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = 'http://localhost:8080/api/auth';

  currentUser = signal<string | null>(this.getStoredUsername());

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(tap(res => this.persistSession(res)));
  }

  /**
   * Renouvelle l'access token via le refresh token stocké.
   * Le backend fait une rotation : l'ancien refresh token est révoqué,
   * un nouveau est émis (à stocker).
   */
  refresh(): Observable<RefreshResponse> {
    const refreshToken = localStorage.getItem(REFRESH_KEY);
    if (!refreshToken) {
      throw new Error('Aucun refresh token disponible');
    }
    return this.http.post<RefreshResponse>(
      `${this.apiUrl}/refresh`,
      { refreshToken }
    ).pipe(
      tap(res => {
        localStorage.setItem(TOKEN_KEY, res.accessToken);
        localStorage.setItem(REFRESH_KEY, res.refreshToken);
      })
    );
  }

  logout(): void {
    const refreshToken = localStorage.getItem(REFRESH_KEY);
    if (refreshToken) {
      this.http.post(`${this.apiUrl}/logout`, { refreshToken }).subscribe({
        error: () => { /* silencieux */ }
      });
    }
    this.clearSession();
  }

  /**
   * Nettoie la session et redirige vers /login.
   * Utilisé après un refresh échoué (refresh token révoqué/expiré).
   */
  clearSession(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_KEY);
    localStorage.removeItem(USERNAME_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  private persistSession(res: LoginResponse): void {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(REFRESH_KEY, res.refreshToken);
    localStorage.setItem(USERNAME_KEY, res.username);
    this.currentUser.set(res.username);
  }

  private getStoredUsername(): string | null {
    return localStorage.getItem(USERNAME_KEY);
  }
}
