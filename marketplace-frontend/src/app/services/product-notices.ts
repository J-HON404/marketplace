import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ProductNotices } from '../interfaces/product-notices';

@Injectable({
  providedIn: 'root'
})
export class NoticeService {
  constructor(private http: HttpClient) {}

  getProductNoticeOfProduct(productId: number): Observable<any[]> {
    return this.http.get<any>(`/api/products/${productId}/notices`).pipe(
      map(res => Array.isArray(res) ? res : res.data || [])
    );
  }

  createProductNotice(productId: number, notice:ProductNotices): Observable<void> {
    return this.http.post<void>(`/api/products/${productId}/notices`, notice);
  }

  deleteProductNotice(productId: number, productNoticeId: number): Observable<any> {
  return this.http.delete(`/api/products/${productId}/notices/${productNoticeId}`);
}

}
