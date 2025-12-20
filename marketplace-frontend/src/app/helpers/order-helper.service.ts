import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrdersHelperService {

  constructor() {}

  canSellerShip(order: any): boolean {
    return order.status === 'READY_TO_ELABORATING';
  }

  isExpired(order: any): boolean {
    return order.status === 'REMIND_DELIVERY' || !!order.expired;
  }

  validateEstimatedDate(dateStr: string): boolean {
    if (!dateStr) return false;
    const parts = dateStr.split('-'); // YYYY-MM-DD
    const estimated = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    estimated.setHours(0, 0, 0, 0);
    return estimated >= today;
  }

  canCustomerConfirm(order: any): boolean {
    if (order.status !== 'SHIPPING_DETAILS_SET') return false;
    if (!order.estimatedDeliveryDate) return false;

    const today = new Date();
    const estimated = new Date(order.estimatedDeliveryDate);
    today.setHours(0, 0, 0, 0);
    estimated.setHours(0, 0, 0, 0);
    return today >= estimated && order.status !== 'CONFIRMED_DELIVERED';
  }
}
