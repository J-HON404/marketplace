import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService, RegisterPayload } from '../../core/services/auth.service';
import { UserRole } from '../../interfaces/profile'; 
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule], 
  templateUrl: './register.html',
  styleUrls: ['./register.scss'] 
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
        // Usiamo il messaggio di successo inviato dal backend Java
        this.registrationMessage = res.message || 'Registrazione completata con successo!';
        
        // Opzionale: reindirizza al login dopo 2 secondi
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        // Estrae l'errore specifico (es. "Username già esistente") dal wrapper ApiResponse
        this.registrationMessage = err.error?.message || 'Errore durante la registrazione';
      }
    });
  }
}