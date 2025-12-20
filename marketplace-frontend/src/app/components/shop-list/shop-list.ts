import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ShopService } from '../../services/shop';
import { TokenService } from '../../services/token';
import { HttpErrorResponse } from '@angular/common/http';
import { Shop } from '../../interfaces/shops';

@Component({
  selector: 'app-shop-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './shop-list.html',
  styleUrls: ['./shop-list.scss']
})
export class ShopListComponent implements OnInit {
  profileId: number | null = null;
  shops: Shop[] = []; 
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService,
    private tokenService: TokenService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.profileId = Number(params.get('profileId')) || this.tokenService.getProfileId();
      if (!this.profileId) {
        this.errorMessage = 'Accesso non autorizzato: ID profilo non trovato';
        return;
      }
      this.loadShops(this.profileId);
    });
  }

  loadShops(profileId: number) {
    this.loading = true;
    this.shopService.getAllShops(profileId).subscribe({
      next: (res: any) => {
        const rawData = res.data || res;
        this.shops = rawData.map((s: any) => ({
          id: s.id,
          name: s.name,
          shopCategory: s.shopCategory,
          profileId: s.profileId || profileId
        }));
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Si è verificato un errore nel caricamento dei negozi.';
        this.loading = false;
      }
    });
  }
}