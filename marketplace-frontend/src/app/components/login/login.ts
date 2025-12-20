import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';
import { TokenService } from '../../services/token';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  username = '';
  password = '';
  loginMessage = '';

  constructor(
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  onLogin() {
    if (!this.username || !this.password) {
      this.loginMessage = 'Compila tutti i campi';
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: res => {
        const token = res.data;
        if (token) {
          this.tokenService.setToken(token);
          const profileId = this.tokenService.getProfileId();
          this.loginMessage = 'Login OK';
          this.router.navigate(['/profile', profileId]);
        } else {
          this.loginMessage = 'Token mancante dal server';
        }
      },
      error: err => this.loginMessage = err.error?.message || 'Errore durante il login'
    });
  }
}