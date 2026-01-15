import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService, RegisterPayload } from '../../core/services/auth.service';
import { UserRole } from '../../models/interfaces/profile'; 
import { RouterModule, Router } from '@angular/router';

/**
 * RegisterComponent è un componente  che gestisce la registrazione di un nuovo utente.
 * Funzioni principali:
 * 1. Raccoglie le informazioni dell'utente tramite form (username, password, email, address e ruolo).
 * 2. Invia i dati di registrazione al backend tramite AuthService.
 * 3. Mostra messaggi di conferma o di errore in base alla risposta del server.
 * 4. Dopo la registrazione, reindirizza automaticamente l'utente alla pagina di login.
 */

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule], 
  templateUrl: '../../views/register/register.html',
  styleUrls: ['../../views/register/register.scss'] 
})
export class RegisterComponent {
  username = '';
  password = '';
  email = '';
  address = '';
  role: UserRole | '' = '';

  roleOptions = [UserRole.CUSTOMER, UserRole.SELLER];
  registrationMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onRegister() {
    if (!this.username || !this.password || !this.role) {
      this.registrationMessage = 'Compila tutti i campi obbligatori';
      return;
    }

    const payload: RegisterPayload = {
      username: this.username,
      password: this.password,
      email: this.email,
      address: this.address,
      role: this.role
    };

    this.authService.register(payload).subscribe({
      next: (res) => {
        this.registrationMessage = res.message || 'Registrazione completata con successo!';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.registrationMessage = err.error?.message || 'Errore durante la registrazione';
      }
    });
  }
}