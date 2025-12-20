import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrdersService } from '../../services/orders';
import { OrdersHelperService } from '../../services/orders-helper';

@Component({
  selector: 'app-orders-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders-table.html',
  styleUrls: ['./orders-table.scss']
})
export class OrdersTableComponent implements OnInit {
  @Input() orders: any[] = [];
  @Input() role!: 'SELLER' | 'CUSTOMER';
  @Input() profileId!: number | null;
  @Input() shopId!: number | null;

  todayString!: string;

  constructor(
    private ordersService: OrdersService,
    private helper: OrdersHelperService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.todayString = new Date().toISOString().split('T')[0];

    this.orders.forEach(order => {
      order._trackingTemp = '';
      order._estimatedTemp = null;
    });

    if (this.role === 'SELLER') {
      this.checkExpiredDeliveries();
    }
  }

  canSellerShip(order: any): boolean {
    return this.helper.canSellerShip(order);
  }

  isExpired(order: any): boolean {
    return this.helper.isExpired(order);
  }

  validateEstimatedDate(dateStr: string): boolean {
    return this.helper.validateEstimatedDate(dateStr);
  }

  shipOrder(order: any) {
    if (!order._trackingTemp || !order._estimatedTemp || !this.shopId) {
      alert('Inserisci Tracking ID e Data Consegna.');
      return;
    }

    if (!this.validateEstimatedDate(order._estimatedTemp)) {
      alert('Non puoi inserire una data di consegna passata.');
      order._estimatedTemp = null;
      return;
    }

    this.ordersService.confirmShipping(
      this.shopId,
      order.id,
      order._trackingTemp,
      order._estimatedTemp
    ).subscribe(() => {
      order.trackingId = order._trackingTemp;
      order.estimatedDeliveryDate = order._estimatedTemp;
      delete order._trackingTemp;
      delete order._estimatedTemp;
      this.cdr.detectChanges();
    });
  }

  checkExpiredDeliveries() {
    if (!this.shopId) return;
    this.ordersService.expiredDeliveries(this.shopId).subscribe((expiredOrderIds: number[]) => {
      this.orders.forEach(order => {
        if (expiredOrderIds.includes(order.id)) {
          order.expired = true;
        }
      });
      this.cdr.detectChanges();
    });
  }

  /* ================= CUSTOMER ================= */
  canCustomerConfirm(order: any): boolean {
    return this.helper.canCustomerConfirm(order);
  }

  confirmDelivery(order: any) {
    if (!this.profileId) return;
    if (order.status === 'CONFIRMED_DELIVERED') return;

    this.ordersService.confirmDelivered(this.profileId, order.id).subscribe(() => {
      order.status = 'CONFIRMED_DELIVERED';
      this.cdr.detectChanges();
    });
  }
}