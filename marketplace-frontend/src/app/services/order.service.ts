import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ApiResponse } from '../models/interfaces/api-response';
import { Order } from '../models/interfaces/order';

/**
 * OrdersService è un servizio che gestisce le chiamate HTTP relative agli ordini.
 * Si occupa di:
 * - Recuperare gli ordini di un negozio o di un profilo cliente.
 * - Confermare la consegna di un ordine da parte del cliente.
 * - Confermare la spedizione di un ordine da parte del venditore.
 * - Recuperare gli ordini scaduti per un negozio.
 */

@Injectable({ providedIn: 'root' })
export class OrdersService {
  constructor(private http: HttpClient) {}

  getShopOrders(shopId: number): Observable<ApiResponse<Order[]>> {
    return this.http.get<ApiResponse<Order[]>>(`/api/shops/${shopId}/orders`);
  }

  getProfileOrders(profileId: number): Observable<ApiResponse<Order[]>> {
    return this.http.get<ApiResponse<Order[]>>(`/api/profiles/${profileId}/orders`);
  }

  confirmDelivered(profileId: number, orderId: number): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(`/api/profiles/${profileId}/orders/${orderId}/confirm-delivered`, null);
  }

  confirmShipping(shopId: number, orderId: number, trackingId: string, estimatedDeliveryDate: string): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(`/api/shops/${shopId}/orders/${orderId}/shipping`, null, {
      params: { trackingId, estimatedDeliveryDate }
    });
  }

  expiredDeliveries(shopId: number): Observable<ApiResponse<number[]>> {
    return this.http.get<ApiResponse<number[]>>(`/api/shops/${shopId}/orders/expired-deliveries`);
  }
}