import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Products } from '../interfaces/product';
import { ApiResponse } from '../interfaces/api-response';

/**
 * ProductService è un servizio che gestisce tutte le operazioni HTTP relative ai prodotti di un negozio.
 * Funzionalità principali:
 * - Recuperare tutti i prodotti di uno shop.
 * - Recuperare un singolo prodotto tramite l'ID.
 * - Creare un nuovo prodotto associato a uno shop.
 * - Aggiornare un prodotto esistente.
 * - Eliminare un prodotto.
 * - Verificare se un profilo è il proprietario dello shop
 */

@Injectable({ providedIn: 'root' })
export class ProductService {
  constructor(private http: HttpClient) { }

  getProducts(shopId: number): Observable<ApiResponse<Products[]>> {
    return this.http.get<ApiResponse<Products[]>>(`/api/shops/${shopId}/products`);
  }

  getProductById(shopId: number, productId: number): Observable<ApiResponse<Products>> {
    return this.http.get<ApiResponse<Products>>(`/api/shops/${shopId}/products/${productId}`);
  }

  createProduct(shopId: number, product: Products): Observable<ApiResponse<Products>> {
    return this.http.post<ApiResponse<Products>>(`/api/shops/${shopId}/products`, product);
  }

  updateProduct(shopId: number, productId: number, product: Products): Observable<ApiResponse<Products>> {
    return this.http.put<ApiResponse<Products>>(`/api/shops/${shopId}/products/${productId}`, product);
  }

  deleteProduct(shopId: number, productId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`/api/shops/${shopId}/products/${productId}`);
  }

  checkOwnership(profileId: number, shopId: number): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`/api/profiles/${profileId}/shops/${shopId}/verify-owner`);
  }
}