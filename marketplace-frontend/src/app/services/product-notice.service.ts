import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductNotices } from '../models/interfaces/product-notice';
import { ApiResponse } from '../models/interfaces/api-response';

/**
 * NoticeService è un servizio che gestisce le chiamate HTTP relative ai Product Notices.
 * Funzionalità principali:
 * - Recuperare tutte le notifiche di un prodotto specifico.
 * - Creare una nuova notifica per un prodotto.
 * - Eliminare una notifica esistente di un prodotto.
 */

@Injectable({
  providedIn: 'root'
})
export class NoticeService {
  constructor(private http: HttpClient) {}

  getProductNoticeOfProduct(productId: number): Observable<ApiResponse<ProductNotices[]>> {
    return this.http.get<ApiResponse<ProductNotices[]>>(`/api/products/${productId}/notices`);
  }

  createProductNotice(productId: number, notice: ProductNotices): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`/api/products/${productId}/notices`, notice);
  }

  deleteProductNotice(productId: number, productNoticeId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`/api/products/${productId}/notices/${productNoticeId}`);
  }
}