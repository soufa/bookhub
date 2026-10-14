import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { AuthService, LoginResponse, RefreshResponse } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;

  const API = 'http://localhost:8080/api/auth';

  const fakeLoginResponse: LoginResponse = {
    token: 'fake.jwt.token',
    refreshToken: 'fake-refresh',
    username: 'admin',
    type: 'Bearer'
  };

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: Router, useValue: routerSpy }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  describe('login', () => {
    it('should store tokens and username after successful login', () => {
      service.login({ username: 'admin', password: 'admin123' }).subscribe(res => {
        expect(res.token).toBe('fake.jwt.token');
      });

      const req = httpMock.expectOne(`${API}/login`);
      expect(req.request.method).toBe('POST');
      req.flush(fakeLoginResponse);

      expect(localStorage.getItem('bookhub.token')).toBe('fake.jwt.token');
      expect(localStorage.getItem('bookhub.refreshToken')).toBe('fake-refresh');
      expect(localStorage.getItem('bookhub.username')).toBe('admin');
      expect(service.currentUser()).toBe('admin');
    });
  });

  describe('logout', () => {
    it('should clear storage and navigate to /login', () => {
      localStorage.setItem('bookhub.token', 'tok');
      localStorage.setItem('bookhub.refreshToken', 'ref');
      localStorage.setItem('bookhub.username', 'admin');

      service.logout();

      const req = httpMock.expectOne(`${API}/logout`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body.refreshToken).toBe('ref');
      req.flush(null);

      expect(localStorage.getItem('bookhub.token')).toBeNull();
      expect(localStorage.getItem('bookhub.refreshToken')).toBeNull();
      expect(localStorage.getItem('bookhub.username')).toBeNull();
      expect(service.currentUser()).toBeNull();
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should not call logout endpoint if no refresh token', () => {
      service.logout();
      httpMock.expectNone(`${API}/logout`);
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  describe('isLoggedIn', () => {
    it('should return false if no token', () => {
      expect(service.isLoggedIn()).toBeFalse();
    });

    it('should return false if token is expired', () => {
      const expiredToken = createFakeJwt(Date.now() / 1000 - 60);
      localStorage.setItem('bookhub.token', expiredToken);
      expect(service.isLoggedIn()).toBeFalse();
    });

    it('should return true if token is valid', () => {
      const validToken = createFakeJwt(Date.now() / 1000 + 3600);
      localStorage.setItem('bookhub.token', validToken);
      expect(service.isLoggedIn()).toBeTrue();
    });

    it('should return false if token is malformed', () => {
      localStorage.setItem('bookhub.token', 'not-a-jwt');
      expect(service.isLoggedIn()).toBeFalse();
    });
  });

  describe('refresh', () => {
    it('should call /refresh and store new tokens', () => {
      localStorage.setItem('bookhub.refreshToken', 'old-refresh');

      const fakeResponse: RefreshResponse = {
        accessToken: 'new-access',
        refreshToken: 'new-refresh'
      };

      service.refresh().subscribe(res => {
        expect(res.accessToken).toBe('new-access');
      });

      const req = httpMock.expectOne(`${API}/refresh`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body.refreshToken).toBe('old-refresh');
      req.flush(fakeResponse);

      expect(localStorage.getItem('bookhub.token')).toBe('new-access');
      expect(localStorage.getItem('bookhub.refreshToken')).toBe('new-refresh');
    });

    it('should throw if no refresh token available', () => {
      expect(() => service.refresh()).toThrowError('Aucun refresh token disponible');
    });
  });

  describe('clearSession', () => {
    it('should clear storage and navigate to /login', () => {
      localStorage.setItem('bookhub.token', 'tok');
      localStorage.setItem('bookhub.refreshToken', 'ref');
      localStorage.setItem('bookhub.username', 'admin');

      service.clearSession();

      expect(localStorage.getItem('bookhub.token')).toBeNull();
      expect(service.currentUser()).toBeNull();
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  // Helper : génère un JWT factice avec un exp donné (en secondes)
  function createFakeJwt(expSeconds: number): string {
    const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
    const payload = btoa(JSON.stringify({ sub: 'admin', exp: expSeconds }));
    return `${header}.${payload}.fake-signature`;
  }
});
