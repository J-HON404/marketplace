import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ShopService } from '../../services/shop.service';
import { TokenService } from '../../services/token';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-shop-list',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './shop-list.html',
  styleUrls: ['./shop-list.scss']
})
export class ShopListComponent implements OnInit {
  profileId: number | null = null;
  shops: any[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService,
    private tokenService: TokenService
  ) {}

  ngOnInit() {
    // ottieni profileId dalla route o dal token
    this.profileId = Number(this.route.snapshot.paramMap.get('profileId')) || this.tokenService.getProfileId();
    if (!this.profileId) {
      this.errorMessage = 'ID profilo non valido';
      return;
    }
    this.loadShops(this.profileId);
  }

  loadShops(profileId: number) {
    this.loading = true;
    this.shopService.getAllShops(profileId).subscribe({
      next: res => {
        // la risposta potrebbe avere un wrapper "data"
        this.shops = res.data || res;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Errore caricamento shop', err);
        this.errorMessage = 'Errore nel caricamento degli shop';
        this.loading = false;
      }
    });
  }
}
