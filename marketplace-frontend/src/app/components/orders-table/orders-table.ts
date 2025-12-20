import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrdersService } from '../../services/orders';
import { OrdersHelperService } from '../../services/order-helper';

@Component({
  selector: 'app-orders-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
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
    public helper: OrdersHelperService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.todayString = new Date().toISOString().split('T')[0];

    this.orders.forEach(order => {
      order._trackingTemp = '';
      order._estimatedTemp = null;
    });

    if (this.role === 'SELLER' && this.shopId) {
      this.checkExpiredDeliveries();
    }
  }

  shipOrder(order: any) {
    if (!this.shopId) return;

    if (!order._trackingTemp || !order._estimatedTemp) {
      alert('Dati di spedizione incompleti.');
      return;
    }

    this.ordersService.confirmShipping(
      this.shopId,
      order.id,
      order._trackingTemp,
      order._estimatedTemp
    ).subscribe({
      next: () => {
        order.trackingId = order._trackingTemp;
        order.estimatedDeliveryDate = order._estimatedTemp;
        order.status = 'SHIPPED';
        delete order._trackingTemp;
        delete order._estimatedTemp;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  confirmDelivery(order: any): void {
    if (!this.profileId) return;

    if (!this.helper.canCustomerConfirm(order)) {
      alert('Azione non consentita: la data di consegna stimata non è ancora passata.');
      return;
    }

    this.ordersService.confirmDelivered(this.profileId, order.id).subscribe({
      next: () => {
        order.status = 'CONFIRMED_DELIVERED';
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  private checkExpiredDeliveries() {
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
}