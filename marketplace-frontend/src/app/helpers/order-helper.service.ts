import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrdersHelperService {

  constructor() {}

  canSellerShip(order: any): boolean {
    return order.status === 'READY_TO_ELABORATING' || order.status === 'PENDING';
  }

  isExpired(order: any): boolean {
    // Un ordine è scaduto se il backend lo ha marcato REMIND_DELIVERY 
    // o se la data stimata è nel passato e non è ancora stato consegnato
    if (order.status === 'CONFIRMED_DELIVERED') return false;
    
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const estimated = order.estimatedDeliveryDate ? new Date(order.estimatedDeliveryDate) : null;
    if (estimated) estimated.setHours(0, 0, 0, 0);

    return order.status === 'REMIND_DELIVERY' || 
           (!!order.expired) || 
           (estimated !== null && today > estimated && order.status === 'SHIPPED');
  }

  canCustomerConfirm(order: any): boolean {
    // L'utente può confermare solo se l'ordine è stato effettivamente spedito
    if (order.status !== 'SHIPPED' && order.status !== 'REMIND_DELIVERY') return false;
    if (!order.estimatedDeliveryDate) return false;

    const today = new Date();
    const estimated = new Date(order.estimatedDeliveryDate);
    
    today.setHours(0, 0, 0, 0);
    estimated.setHours(0, 0, 0, 0);

    // Può confermare se OGGI è uguale o successivo alla data stimata
    return today >= estimated;
  }
}