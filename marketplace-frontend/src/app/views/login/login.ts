import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { TokenService } from '../../core/services/token.service';

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
      next: (res) => {
        // Supponendo che il backend risponda con ApiResponse<string> dove string è il JWT
        const token = res.data; 
        
        if (token) {
          this.tokenService.setToken(token);
          
          // Recuperiamo il profileId dal token appena salvato
          const profileId = this.tokenService.getProfileId();
          this.loginMessage = 'Login effettuato con successo!';

          // Navighiamo al profilo
          if (profileId) {
            this.router.navigate(['/profile', profileId]);
          } else {
            // Fallback se il token non contiene il profileId correttamente
            this.router.navigate(['/home']);
          }
        } else {
          this.loginMessage = 'Risposta del server non valida.';
        }
      },
      error: (err) => {
        // Usiamo il messaggio che arriva dal tuo DTO Java ApiResponse
        this.loginMessage = err.error?.message || 'Credenziali non valide o errore server.';
      }
    });
  }
}