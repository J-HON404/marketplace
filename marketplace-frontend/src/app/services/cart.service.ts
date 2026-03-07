import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../models/interfaces/order';
import{ Cart } from '../models/interfaces/cart';
import { ApiResponse } from '../models/interfaces/api-response';

/**
 * CartService
 * Questo servizio Angular gestisce le chiamate HTTP relative al carrello (Cart) di un utente.
 * Funzionalità principali:
 * - Recupera il carrello di un utente per uno specifico shop.
 * - Crea un nuovo carrello se non esiste.
 * - Aggiunge o rimuove prodotti dal carrello.
 * - Aggiorna la quantità di un prodotto presente nel carrello.
 * - Svuota completamente il carrello.
 * - Effettua il checkout del carrello, creando un ordine lato backend.
 */

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

  checkout(profileId: number, shopId: number): Observable<ApiResponse<Order>> {
    return this.http.post<ApiResponse<Order>>(`/api/profiles/${profileId}/shops/${shopId}/cart/checkout`, {});
  }
}