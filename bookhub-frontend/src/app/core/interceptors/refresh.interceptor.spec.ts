import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { refreshInterceptor } from './refresh.interceptor';
import { AuthService } from '../services/auth.service';
import { of, throwError } from 'rxjs';

describe('refreshInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let authSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authSpy = jasmine.createSpyObj('AuthService', ['refresh', 'clearSession', 'getToken']);

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([refreshInterceptor])),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: authSpy }
      ]
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should pass through successful requests', () => {
    authSpy.refresh.and.returnValue(of({ accessToken: 'new', refreshToken: 'r' }));

    http.get('/api/books').subscribe();

    const req = httpMock.expectOne('/api/books');
    req.flush({ content: [] });

    expect(authSpy.refresh).not.toHaveBeenCalled();
  });

  it('should NOT try to refresh on /api/auth/** 401 (avoid infinite loop)', () => {
    authSpy.refresh.and.returnValue(throwError(() => new Error('never called')));

    let caughtError: any;
    http.post('/api/auth/login', {}).subscribe({
      error: (err) => caughtError = err
    });

    const req = httpMock.expectOne('/api/auth/login');
    req.flush({ error: 'bad credentials' }, { status: 401, statusText: 'Unauthorized' });

    expect(authSpy.refresh).not.toHaveBeenCalled();
    expect(caughtError).toBeTruthy();
  });

  it('should refresh token and retry on 401', () => {
    authSpy.refresh.and.returnValue(of({
      accessToken: 'new-access',
      refreshToken: 'new-refresh'
    }));

    let response: any;
    http.get('/api/books').subscribe(res => response = res);

    // 1er appel → 401
    const firstReq = httpMock.expectOne('/api/books');
    firstReq.flush({ error: 'expired' }, { status: 401, statusText: 'Unauthorized' });

    // Le refresh est appelé
    expect(authSpy.refresh).toHaveBeenCalled();

    // 2ème appel → 200 avec le nouveau token
    const retriedReq = httpMock.expectOne('/api/books');
    expect(retriedReq.request.headers.get('Authorization')).toBe('Bearer new-access');
    retriedReq.flush({ content: [] });

    expect(response).toEqual({ content: [] });
  });

  it('should clear session if refresh fails', () => {
    authSpy.refresh.and.returnValue(
      throwError(() => ({ status: 401, statusText: 'Unauthorized' }))
    );

    let caughtError: any;
    http.get('/api/books').subscribe({
      error: (err) => caughtError = err
    });

    const firstReq = httpMock.expectOne('/api/books');
    firstReq.flush({ error: 'expired' }, { status: 401, statusText: 'Unauthorized' });

    expect(authSpy.clearSession).toHaveBeenCalled();
    expect(caughtError).toBeTruthy();
  });
});
