import { Component } from '@angular/core';
import { TokenService } from '../../core/services/token.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class HomeComponent {
  role: string | null;
  profileId: number | null;

  constructor(private tokenService: TokenService, private router:Router) {
    this.role = this.tokenService.getUserRole();
    this.profileId = this.tokenService.getProfileId();
  }

   logout() {
    this.tokenService.clearToken(); // rimuove il token
    this.router.navigate(['/login']); // reindirizza alla login
  }
}
