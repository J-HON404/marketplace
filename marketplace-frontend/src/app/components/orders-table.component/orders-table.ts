import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrdersService } from '../../services/order.service';
import { OrdersHelperService } from '../../helpers/order-helper.service';

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
    this.initializeTempFields();

    if (this.role === 'SELLER' && this.shopId) {
      this.checkExpiredDeliveries();
    }
  }

  // Inizializza i campi solo se non esistono già (evita sovrascritture)
  private initializeTempFields() {
    this.orders.forEach(order => {
      order._trackingTemp = order._trackingTemp || order.trackingId || '';
      order._estimatedTemp = order._estimatedTemp || order.estimatedDeliveryDate || null;
    });
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
      next: (res) => {
        // AGGIORNAMENTO STATO: Deve corrispondere a quello cercato dall'helper
        order.status = 'SHIPPED'; 
        order.trackingId = order._trackingTemp;
        order.estimatedDeliveryDate = order._estimatedTemp;
        
        // Pulizia campi temporanei
        delete order._trackingTemp;
        delete order._estimatedTemp;
        
        this.cdr.detectChanges();
        alert(res.message || 'Ordine spedito con successo!');
      },
      error: (err) => alert(err.error?.message || 'Errore durante la spedizione')
    });
  }

  confirmDelivery(order: any): void {
    if (!this.profileId) return;

    if (!this.helper.canCustomerConfirm(order)) {
      alert('Non puoi ancora confermare la consegna.');
      return;
    }

    this.ordersService.confirmDelivered(this.profileId, order.id).subscribe({
      next: (res) => {
        order.status = 'CONFIRMED_DELIVERED';
        this.cdr.detectChanges();
        alert(res.message || 'Consegna confermata!');
      },
      error: (err) => alert(err.error?.message || 'Errore nella conferma consegna')
    });
  }

  private checkExpiredDeliveries() {
    if (!this.shopId) return;
    this.ordersService.expiredDeliveries(this.shopId).subscribe({
      next: (res) => {
        const expiredOrderIds = res.data || [];
        this.orders.forEach(order => {
          if (expiredOrderIds.includes(order.id)) {
            order.expired = true;
          }
        });
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Errore controllo scadenze:', err)
    });
  }
}