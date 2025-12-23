import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../core/services/token.service';
import { ShopService } from '../../services/shop.service';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class HomeComponent implements OnInit {
  role: string | null;
  profileId: number | null;
  shopId: number | null = null;

  constructor(
    private tokenService: TokenService, 
    private router: Router,
    private shopService: ShopService
   ) {
    this.role = this.tokenService.getUserRole();
    this.profileId = this.tokenService.getProfileId();
  }

  ngOnInit(): void {
    if (this.role === 'ROLE_SELLER' && this.profileId) {
      this.shopService.getShop(this.profileId).subscribe({
        next: (res) => {
          if (res.data) {
            this.shopId = res.data.id;
          }
        },
        error: () => {
          this.shopId = null; 
        }
      });
    }
  }

  logout() {
    this.tokenService.clearToken();
    this.router.navigate(['/login']);
  }
}