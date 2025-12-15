import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { AuthService, RegisterPayload } from '../../services/auth.service';
import { UserRole } from '../../services/user-role.enum';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule,RouterModule], 
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

  constructor(private authService: AuthService) {}

  onRegister() {

    if (!this.username || !this.password || !this.role) {
      this.registrationMessage = 'Compila tutti i campi';
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
      next: () => this.registrationMessage = 'Registrazione OK',
      error: err  => this.registrationMessage = err.error?.message || 'Errore'
    });
}
}
