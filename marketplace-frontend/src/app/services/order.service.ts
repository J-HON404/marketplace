import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ApiResponse } from '../interfaces/api-response';

@Injectable({ providedIn: 'root' })
export class OrdersService {
  constructor(private http: HttpClient) {}

  getShopOrders(shopId: number): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`/api/shops/${shopId}/orders`);
  }

  getProfileOrders(profileId: number): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`/api/profiles/${profileId}/orders`);
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