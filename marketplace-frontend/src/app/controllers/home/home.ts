import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../core/services/token.service';
import { ShopService } from '../../services/shop.service';
import { AuthService } from '../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

/**
 * HomeComponent: pagina principale dell'applicazione.
 * - Recupera ruolo e profileId dal token tramite TokenService
 * - Se l'utente è SELLER, recupera lo shop associato tramite ShopService
 * - Gestisce il logout lato client e server
 */

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: '../../views/home/home.html',
  styleUrls: ['../../views/home/home.scss']
})
export class HomeComponent implements OnInit {
  role: string | null;
  profileId: number | null;
  shopId: number | null = null;

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private shopService: ShopService,
    private authService: AuthService 
  ) {
    this.role = this.tokenService.getUserRole();
    this.profileId = this.tokenService.getProfileId();
  }

  ngOnInit(): void {
    if (this.role === 'ROLE_SELLER' && this.profileId) {
      this.shopService.getShop(this.profileId).subscribe({
        next: (res) => {
          this.shopId = res.data.id;
        },
        error: () => {
          this.shopId = null; 
        }
      });
    }
  }

  logout(): void {
    const token = this.tokenService.getToken();
    
    if (token) {
      // Chiamata al backend per invalidare il token
      this.authService.logout().subscribe({
        next: () => this.clearAndRedirect(),
        error: () => this.clearAndRedirect()
      });
    } else {
      // Nessun token presente: logout locale
      this.clearAndRedirect();
    }
  }

  private clearAndRedirect(): void {
    this.tokenService.clearToken(); // rimuove token dal client
    this.router.navigate(['/login']); // reindirizza a login
  }
}