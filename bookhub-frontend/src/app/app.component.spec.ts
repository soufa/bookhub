import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AppComponent } from './app.component';
import { AuthService } from './core/services/auth.service';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        {
          provide: AuthService,
          useValue: {
            isLoggedIn: () => false,
            currentUser: () => null,
            logout: () => {}
          }
        }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should call authService.logout() on logout()', () => {
    const authMock = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    spyOn(authMock, 'logout');

    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.logout();
    expect(authMock.logout).toHaveBeenCalled();
  });
});
