import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../services/profile';
import { OrdersService } from '../../services/orders';
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
  ordersTitle = '';
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private profileService: ProfileService,
    private ordersService: OrdersService,
    private tokenService: TokenService
  ) {}

  ngOnInit() {
    this.profileId = Number(this.route.snapshot.paramMap.get('id')) || this.tokenService.getProfileId();
    if (!this.profileId) {
      this.errorMessage = 'ID profilo non valido';
      return;
    }
    this.fetchProfile(this.profileId);
  }

  fetchProfile(id: number) {
    this.loading = true;
    this.profileService.getProfile(id).subscribe({
      next: res => {
        this.profileData = res.data || res;
        this.loading = false;

        const role = this.tokenService.getUserRole();
        if (!role) {
          this.errorMessage = 'Ruolo non disponibile';
          return;
        }

        const roleUpper = role.toUpperCase();
        const shopId = this.tokenService.getShopId();

        this.ordersTitle = roleUpper === 'SELLER' ? 'Ordini ricevuti' : 'Ordini fatti';
        this.loadOrders(roleUpper, id, shopId);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Errore caricamento profilo', err);
        this.errorMessage = 'Errore nel caricamento del profilo';
        this.loading = false;
      }
    });
  }

  loadOrders(role: string, profileId: number, shopId: number | null) {
    this.loading = true;

    if (role === 'SELLER') {
      if (!shopId) {
        this.errorMessage = 'Shop ID non disponibile per il seller';
        this.loading = false;
        return;
      }

      this.ordersService.getShopOrders(shopId).subscribe({
        next: data => {
          this.orders = data;
          this.loading = false;
        },
        error: (err: HttpErrorResponse) => {
          console.error('Errore caricamento ordini shop', err);
          this.errorMessage = 'Errore nel caricamento degli ordini';
          this.loading = false;
        }
      });

    } else {
      this.ordersService.getProfileOrders(profileId).subscribe({
        next: data => {
          this.orders = data;
          this.loading = false;
        },
        error: (err: HttpErrorResponse) => {
          console.error('Errore caricamento ordini profilo', err);
          this.errorMessage = 'Errore nel caricamento degli ordini';
          this.loading = false;
        }
      });
    }
  }
}
