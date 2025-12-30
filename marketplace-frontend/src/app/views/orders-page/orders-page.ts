import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrdersService } from '../../services/order.service';
import { TokenService } from '../../core/services/token.service';
import { OrdersTableComponent } from '../../components/orders-table.component/orders-table';
import { Order } from '../../interfaces/order';

/**
 * OrdersPageComponent è una componente il cui scopo è mostrare gli ordini di un profilo
 * Funzioni principali:
 * 1. Determina il ruolo dell'utente (SELLER o CUSTOMER) leggendo il token JWT tramite TokenService.
 * 2. Se l'utente è un SELLER, mostra gli ordini ricevuti dal negozio associato al suo account.
 * 3. Se l'utente è un CUSTOMER, mostra gli ordini effettuati dal profilo loggato.
 * 4. Utilizza OrdersService per recuperare gli ordini dal backend.
 * 7. Visualizza gli ordini usando il componente OrdersTableComponent.
 */

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