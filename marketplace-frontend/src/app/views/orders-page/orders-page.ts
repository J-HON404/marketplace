import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrdersService } from '../../services/order.service';
import { TokenService } from '../../core/services/token.service';
import { OrdersTableComponent } from '../../components/orders-table.component/orders-table';

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

  if (!role) return;

  this.loading = true;

  if (role === 'SELLER') {
    if (!shopId) return;

    this.ordersTitle = 'Ordini ricevuti';

    this.ordersService.getShopOrders(shopId).subscribe({
      next: data => {
        this.orders = data;
        this.loading = false;
      }
    });

  } else {
    if (!profileId) return;

    this.ordersTitle = 'Ordini fatti';

    this.ordersService.getProfileOrders(profileId).subscribe({
      next: data => {
        this.orders = data;
        this.loading = false;
      }
    });
  }
}
}

