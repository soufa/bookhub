import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';   // ← AJOUTER
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],   // ← AJOUTER CommonModule
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';

  loading = signal(false);
  error = signal<string | null>(null);

  onSubmit(): void {
    if (!this.username || !this.password) {
      this.error.set('Veuillez remplir tous les champs');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.auth.login({ username: this.username, password: this.password })
      .subscribe({
        next: () => {
          this.loading.set(false);
          this.router.navigate(['/books']);
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(err.status === 401
            ? 'Identifiants invalides'
            : `Erreur ${err.status}`);
        }
      });
  }
}
