import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../core/services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authSpy = jasmine.createSpyObj('AuthService', ['login']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not submit if fields are empty', () => {
    component.username = '';
    component.password = '';
    component.onSubmit();
    expect(authSpy.login).not.toHaveBeenCalled();
  });

  it('should navigate to /books on successful login', () => {
    authSpy.login.and.returnValue(of({
      token: 'tok', refreshToken: 'ref', username: 'admin', type: 'Bearer'
    }));

    component.username = 'admin';
    component.password = 'admin123';
    component.onSubmit();

    expect(authSpy.login).toHaveBeenCalledWith({ username: 'admin', password: 'admin123' });
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/books']);
  });

  it('should show error message on 401', () => {
    authSpy.login.and.returnValue(throwError(() => ({ status: 401 })));

    component.username = 'admin';
    component.password = 'wrong';
    component.onSubmit();

    expect(component.error()).toBe('Identifiants invalides');
    expect(component.loading()).toBeFalse();
  });

  it('should not submit if fields are empty', () => {
    component.username = '';
    component.password = '';
    component.onSubmit();

    expect(authSpy.login).not.toHaveBeenCalled();
    expect(component.error()).toBeTruthy();
  });
});
