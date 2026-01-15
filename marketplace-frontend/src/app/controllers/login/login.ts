import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { TokenService } from '../../core/services/token.service';

/**
 * LoginComponent gestisce la pagina di login dell'applicazione.
 * Funzioni principali:
 * 1. Gestisce i campi di input username e password.
 * 2. Interagisce con AuthService per inviare le credenziali al backend.
 * 3. Se il login ha successo, salva il token JWT tramite TokenService.
 * 4. Recupera il profileId dal token e reindirizza l'utente alla home
 */

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: '../../views/login/login.html',
  styleUrls: ['../../views/login/login.scss']
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
      next: (res) => {
        const token = res.data; 
        
        if (token) {
          this.tokenService.setToken(token);
          const profileId = this.tokenService.getProfileId();
          this.loginMessage = 'Login effettuato con successo!';
          if (profileId) {
            this.router.navigate(['/profile', profileId]);
          } else {
            this.router.navigate(['/home']);
          }
        } else {
          this.loginMessage = 'Risposta del server non valida.';
        }
      },
      error: (err) => {
        this.loginMessage = err.error?.message || 'Credenziali non valide o errore server.';
      }
    });
  }
}