import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ShopService } from '../../services/shop.service';
import { TokenService } from '../../core/services/token.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Shop } from '../../models/interfaces/shops';

/**
 * ShopListComponent è un componente che mostra la lista dei negozi associati a un profilo.
 * Funzioni principali:
 * 1. Recupera l'ID del profilo dalla route o dal token dell'utente loggato.
 * 2. Carica tutti i negozi associati a quel profilo tramite ShopService.
 */

@Component({
  selector: 'app-shop-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: '../../views/shop-list/shop-list.html',
  styleUrls: ['../../views/shop-list/shop-list.scss']
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
      next: (res) => {
        const rawData = res.data || [];
        this.shops = rawData.map((s: Shop) => ({
          ...s,
          profileId: s.profileId || profileId
        }));
        
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = err.error?.message || 'Si è verificato un errore nel caricamento dei negozi.';
        this.loading = false;
      }
    });
  }
}