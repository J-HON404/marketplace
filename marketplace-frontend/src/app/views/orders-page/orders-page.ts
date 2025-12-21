import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrdersService } from '../../services/order.service';
import { TokenService } from '../../core/services/token.service';
import { OrdersTableComponent } from '../../components/orders-table.component/orders-table';

// Verifica che questi campi siano coerenti con il DTO Java
interface Order {
  id: number;
  date: string;
  total: number;
  status: string;
}

@Component({
  selector: 'app-orders-page',
  standalone: true,
  imports: [CommonModule, OrdersTableComponent],
  templateUrl: './orders-page.html',
  styleUrls: ['./orders-page.scss']
})
export class OrdersPageComponent implements OnInit {
  orders: Order[] = [];
  ordersTitle = '';
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private ordersService: OrdersService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    const role = this.tokenService.getUserRole();
    const profileId = this.tokenService.getProfileId();
    const shopId = this.tokenService.getShopId();

    if (!role) {
      this.errorMessage = 'Ruolo non trovato';
      return;
    }

    this.loading = true;

    if (role === 'SELLER') {
      if (!shopId) {
        this.loading = false;
        this.errorMessage = 'ID Negozio non trovato';
        return;
      }

      this.ordersTitle = 'Ordini ricevuti';

      this.ordersService.getShopOrders(shopId).subscribe({
        next: res => {
          // Accediamo a .data
          this.orders = res.data;
          this.loading = false;
        },
        error: () => {
          this.errorMessage = 'Errore nel caricamento degli ordini ricevuti';
          this.loading = false;
        }
      });

    } else {
      if (!profileId) {
        this.loading = false;
        this.errorMessage = 'ID Profilo non trovato';
        return;
      }

      this.ordersTitle = 'Ordini effettuati';

      this.ordersService.getProfileOrders(profileId).subscribe({
        next: res => {
          // Accediamo a .data
          this.orders = res.data;
          this.loading = false;
        },
        error: () => {
          this.errorMessage = 'Errore nel caricamento dei tuoi ordini';
          this.loading = false;
        }
      });
    }
  }
}