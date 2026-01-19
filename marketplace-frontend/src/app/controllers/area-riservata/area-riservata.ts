import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../services/profile.service';
import { OrdersService } from '../../services/order.service';
import { ShopService } from '../../services/shop.service';
import { TokenService } from '../../core/services/token.service';
import { OrdersTableComponent } from '../../common/components/orders-table.component/orders-table';
import { HttpErrorResponse } from '@angular/common/http';
import { Order } from '../../models/interfaces/order';
import { Profile } from '../../models/interfaces/profile';
import { Shop } from '../../models/interfaces/shops';

/**
 * AreaRiservataComponent è un componente che gestisce la pagina dell'area riservata 
 * di un utente, mostrando informazioni sul profilo, sul negozio (se è un seller) e sugli ordini.
 * Funzionalità principali:
 * - Recupera l'ID del profilo dall'URL o dal token JWT.
 * - Carica i dati del profilo tramite ProfileService.
 * - Determina il ruolo dell'utente (SELLER o CUSTOMER) e adatta il comportamento della pagina.
 * - Se l'utente è un SELLER, carica le informazioni del negozio associato tramite ShopService.
 * - Carica gli ordini dell'utente o del negozio tramite OrdersService.
 */
@Component({
  selector: 'app-area-riservata',
  standalone: true,
  imports: [CommonModule, RouterModule, OrdersTableComponent],
  templateUrl: '../../views/area-riservata/area-riservata.html',
  styleUrls: ['../../views/area-riservata/area-riservata.scss']
})
export class AreaRiservataComponent implements OnInit {
  profileId: number | null = null;
  profileData: Profile | null = null; 
  orders: Order[] = [];
  loading = false;
  errorMessage = '';
  roleUpper!: 'SELLER' | 'CUSTOMER';
  shopId: number | null = null;         
  shopData: Shop | null = null;


  constructor(
    private route: ActivatedRoute,
    private profileService: ProfileService,
    private ordersService: OrdersService,
    private tokenService: TokenService,
    private shopService: ShopService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.profileId = Number(params.get('id')) || this.tokenService.getProfileId();
      if (!this.profileId) {
        this.errorMessage = 'ID profilo non valido';
        return;
      }
      this.fetchProfile(this.profileId);
    });
  }

  fetchProfile(id: number) {
    this.loading = true;
    this.profileService.getProfile(id).subscribe({
      next: res => {
        this.profileData = res.data;
        this.loading = false;
        if (!this.profileData || !this.profileData.role) {
          this.errorMessage = 'Profilo o ruolo non disponibile';
          return;
        }
        this.roleUpper = this.profileData.role.toUpperCase() as 'SELLER' | 'CUSTOMER';
        this.shopId = this.tokenService.getShopId(); // può essere null
        if (this.roleUpper === 'SELLER') {
          this.loadShopInfo();
        } else {
          this.loadOrders();
        }
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = 'Errore nel caricamento del profilo';
        this.loading = false;
      }
    });
  }

loadOrders() {
  if (this.roleUpper === 'SELLER' && this.shopId != null) {
    this.ordersService.getShopOrders(this.shopId).subscribe({
      next: res => this.orders = res.data,
      error: () => this.errorMessage = 'Errore nel caricamento ordini'
    });
    return;
  }

  if (this.roleUpper === 'CUSTOMER' && this.profileId != null) {
    this.ordersService.getProfileOrders(this.profileId).subscribe({
      next: res => this.orders = res.data,
      error: () => this.errorMessage = 'Errore nel caricamento ordini'
    });
  }
}


  loadShopInfo() {
    if (!this.profileId) return;
    this.shopService.getShop(this.profileId).subscribe({
      next: res => {
        this.shopData = res.data; 
        if (this.shopData) {
          this.shopId = this.shopData.id;
          this.loadOrders();
        } else {
          this.shopId = null;
        }
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 404) {
          this.shopData = null;
          this.shopId = null;
        } else {
          this.errorMessage = 'Errore nel caricamento dello shop';
        }
      }
    });
  }
}