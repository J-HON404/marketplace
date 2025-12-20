import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cart } from '../interfaces/cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient) {}


  getCart(profileId: number, shopId: number): Observable<Cart> {
    return this.http.get<Cart>(`/api/profiles/${profileId}/shops/${shopId}/cart`);
  }

  createCart(profileId: number, shopId: number): Observable<Cart> {
    return this.http.post<Cart>(`/api/profiles/${profileId}/shops/${shopId}/cart/create`, {});
  }

  removeProductFromCart(profileId: number, shopId: number, productId: number): Observable<void> {
    return this.http.delete<void>(`/api/profiles/${profileId}/shops/${shopId}/cart/remove/${productId}`);
  }

  addProductToCart(profileId: number, shopId: number, productId: number, quantity: number): Observable<Cart> {
    return this.http.post<Cart>(`/api/profiles/${profileId}/shops/${shopId}/cart/add`, { productId, quantity });
  }

  clearCart(profileId: number, shopId: number): Observable<void> {
    return this.http.delete<void>(`/api/profiles/${profileId}/shops/${shopId}/cart/clear`);
  }

  updateProductQuantity(profileId: number, shopId: number, productId: number, quantity: number): Observable<Cart> {
    return this.http.put<Cart>(`/api/profiles/${profileId}/shops/${shopId}/cart/update`, { productId, quantity });
  }

  checkout(profileId: number, shopId: number): Observable<any> {
    return this.http.post<any>(`/api/profiles/${profileId}/shops/${shopId}/cart/checkout`, {});
  }
}