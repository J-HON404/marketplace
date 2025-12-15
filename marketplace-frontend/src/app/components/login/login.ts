import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule,RouterModule], 
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  username = '';
  password = '';
  loginMessage = '';

  constructor(private authService: AuthService) {}

  onLogin() {
    if (!this.username || !this.password) {
      this.loginMessage = 'Compila tutti i campi';
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: res => this.loginMessage = 'Login OK',
      error: err => this.loginMessage = err.error?.message || 'Errore'
    });
  }
}
