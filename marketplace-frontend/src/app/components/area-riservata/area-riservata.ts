import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../services/profile';
import { OrdersService } from '../../services/orders';
import { ShopService } from '../../services/shop';
import { TokenService } from '../../services/token';
import { OrdersTableComponent } from '../orders-table/orders-table';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-area-riservata',
  standalone: true,
  imports: [CommonModule, RouterModule, OrdersTableComponent],
  templateUrl: './area-riservata.html',
  styleUrls: ['./area-riservata.scss']
})
export class AreaRiservataComponent implements OnInit {
  profileId: number | null = null;
  profileData: any = null;
  orders: any[] = [];
  loading = false;
  errorMessage = '';
  roleUpper!: 'SELLER' | 'CUSTOMER';
  shopId: number | null = null;
  shopData: any = null;

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
        this.profileData = res.data || res;
        this.loading = false;
        if (!this.profileData.role) {
          this.errorMessage = 'Ruolo non disponibile';
          return;
        }
        this.roleUpper = this.profileData.role.toUpperCase() as 'SELLER' | 'CUSTOMER';
        this.shopId = this.tokenService.getShopId();
        if (this.roleUpper === 'SELLER' && this.shopId) {
          this.loadShopInfo();
        }
        this.loadOrders();
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = 'Errore nel caricamento del profilo';
        this.loading = false;
      }
    });
  }

  loadOrders() {
    if (this.roleUpper === 'SELLER') {
      if (!this.shopId) return;
      this.ordersService.getShopOrders(this.shopId).subscribe({
        next: orders => this.orders = orders,
        error: () => this.errorMessage = 'Errore nel caricamento ordini'
      });
    } else {
      this.ordersService.getProfileOrders(this.profileId!).subscribe({
        next: orders => this.orders = orders,
        error: () => this.errorMessage = 'Errore nel caricamento ordini'
      });
    }
  }

  loadShopInfo() {
    if (!this.profileId) return;
    this.shopService.getShop(this.profileId).subscribe({
      next: res => this.shopData = res.data,
      error: () => this.errorMessage = 'Errore nel caricamento dello shop'
    });
  }
}