import { Injectable } from '@angular/core';
import { Order } from '../../models/interfaces/order';

/**
 * OrdersHelperService fornisce funzioni di supporto per verificare lo stato degli ordini
 * sia dal punto di vista del venditore (SELLER) sia del cliente (CUSTOMER). 
 * Restituisce informazioni booleane utili per UI o logica di controllo.
 * Funzioni principali:
 * - canSellerShip(order): verifica se l'ordine è pronto per la spedizione da parte del venditore.
 * - isExpired(order): controlla se la data stimata di consegna dell'ordine è passata e l'ordine non è ancora confermato come consegnato.
 * - canCustomerConfirm(order): controlla se il cliente può confermare la consegna dell'ordine, basandosi sullo stato e sulla data stimata.
 */

@Injectable({
  providedIn: 'root'
})
export class OrdersHelperService {

  canSellerShip(order: Order): boolean {
    return order.status === 'READY_TO_ELABORATING' || order.status === 'PENDING';
  }

  isExpired(order: Order): boolean {
    if (!order.estimatedDeliveryDate || order.status === 'CONFIRMED_DELIVERED') return false;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const estimated = new Date(order.estimatedDeliveryDate);
    estimated.setHours(0, 0, 0, 0);
    return today > estimated; 
  }

canCustomerConfirm(order: Order): boolean {
  if (order.status !== 'SHIPPING_DETAILS_SET' && order.status !== 'SHIPPED' && order.status !== 'REMIND_DELIVERY') {
    return false;
  }
  if (!order.estimatedDeliveryDate) return false;
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const estimated = new Date(order.estimatedDeliveryDate);
  estimated.setHours(0, 0, 0, 0);
  return today >= estimated;
}

}