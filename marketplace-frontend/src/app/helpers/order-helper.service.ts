import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrdersHelperService {

  canSellerShip(order: any): boolean {
    return order.status === 'READY_TO_ELABORATING' || order.status === 'PENDING';
  }


  isExpired(order: any): boolean {
    if (!order.estimatedDeliveryDate || order.status === 'CONFIRMED_DELIVERED') return false;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const estimated = new Date(order.estimatedDeliveryDate);
    estimated.setHours(0, 0, 0, 0);
    return today > estimated; 
  }

canCustomerConfirm(order: any): boolean {
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