import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  constructor(private http: HttpClient) {}

  getShopOrders(shopId: number): Observable<any[]> {
    return this.http.get<any>(`/api/shops/${shopId}/orders`).pipe(
      map(res => Array.isArray(res) ? res : res.data || [])
    );
  }

  getProfileOrders(profileId: number): Observable<any[]> {
    return this.http.get<any>(`/api/profiles/${profileId}/orders`).pipe(
      map(res => Array.isArray(res) ? res : res.data || [])
    );
  }

  confirmDelivered(profileId: number, orderId: number): Observable<void> {
    return this.http.put<void>(`/api/profiles/${profileId}/orders/${orderId}/confirm-delivered`, null);
  }
}
