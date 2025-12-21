import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cart } from '../interfaces/cart';
import { ApiResponse } from '../interfaces/api-response';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient) {}

  getCart(profileId: number, shopId: number): Observable<ApiResponse<Cart>> {
    return this.http.get<ApiResponse<Cart>>(`/api/profiles/${profileId}/shops/${shopId}/cart`);
  }

  createCart(profileId: number, shopId: number): Observable<ApiResponse<Cart>> {
    return this.http.post<ApiResponse<Cart>>(`/api/profiles/${profileId}/shops/${shopId}/cart/create`, {});
  }

  removeProductFromCart(profileId: number, shopId: number, productId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`/api/profiles/${profileId}/shops/${shopId}/cart/remove/${productId}`);
  }

  addProductToCart(profileId: number, shopId: number, productId: number, quantity: number): Observable<ApiResponse<Cart>> {
    return this.http.post<ApiResponse<Cart>>(`/api/profiles/${profileId}/shops/${shopId}/cart/add`, { productId, quantity });
  }

  clearCart(profileId: number, shopId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`/api/profiles/${profileId}/shops/${shopId}/cart/clear`);
  }

  updateProductQuantity(profileId: number, shopId: number, productId: number, quantity: number): Observable<ApiResponse<Cart>> {
    return this.http.put<ApiResponse<Cart>>(`/api/profiles/${profileId}/shops/${shopId}/cart/update`, { productId, quantity });
  }

  checkout(profileId: number, shopId: number): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`/api/profiles/${profileId}/shops/${shopId}/cart/checkout`, {});
  }
}