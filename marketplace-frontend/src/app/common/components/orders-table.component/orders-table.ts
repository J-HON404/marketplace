import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrdersService } from '../../../services/order.service';
import { OrdersHelperService } from '../../helpers/order-helper.service';
import { Order } from '../../../models/interfaces/order';

/**
 * Componente Angular che visualizza e gestisce una tabella di ordini.
 *
 * Il componente è riutilizzabile sia per utenti CUSTOMER che SELLER e
 * adatta il comportamento in base al ruolo passato in input.
 *
 * Funzionalità principali:
 * - Visualizzazione degli ordini ricevuti 
 * - Gestione della spedizione degli ordini da parte del SELLER
 * - Conferma della consegna da parte del CUSTOMER
 * - Controllo automatico degli ordini con consegna scaduta (per SELLER)
 *
 * Utilizza OrdersService per comunicare con il backend e
 * OrdersHelperService per la logica di supporto lato UI.
 * ChangeDetectorRef è usato per forzare l'aggiornamento della vista
 * dopo operazioni asincrone.
 */

@Component({
  selector: 'app-orders-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './orders-table.html',
  styleUrls: ['./orders-table.scss']
})
export class OrdersTableComponent implements OnInit {
  // Estendiamo Order con le proprietà aggiuntive usate dal componente
  @Input() orders: (Order & {
    _trackingTemp?: string;
    _estimatedTemp?: string | null;
    trackingId?: string;
    estimatedDeliveryDate?: string | null;
    expired?: boolean;
  })[] = [];

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

  private initializeTempFields() {
    this.orders.forEach(order => {
      order._trackingTemp = order._trackingTemp || order.trackingId || '';
      order._estimatedTemp = order._estimatedTemp || order.estimatedDeliveryDate || null;
    });
  }

  shipOrder(order: Order & {
    _trackingTemp?: string;
    _estimatedTemp?: string | null;
    trackingId?: string;
    estimatedDeliveryDate?: string | null;
    expired?: boolean;
  }) {
    if (this.shopId == null) return;
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
      order.trackingId = order._trackingTemp;
      order.estimatedDeliveryDate = order._estimatedTemp;
      order._trackingTemp = undefined;
      order._estimatedTemp = undefined;
      order.expired = order.expired ?? false;
      this.cdr.detectChanges();
      alert(res.message || 'Ordine spedito con successo!');
    },
    error: (err) => alert(err.error?.message || 'Errore durante la spedizione')
  });
  }

  confirmDelivery(order: Order & {
    _trackingTemp?: string;
    _estimatedTemp?: string | null;
    trackingId?: string;
    estimatedDeliveryDate?: string | null;
    expired?: boolean;
  }): void {
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
        const expiredOrderIds: number[] = res.data || [];
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