import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductNotices } from '../interfaces/product-notice';
import { ApiResponse } from '../interfaces/api-response';

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