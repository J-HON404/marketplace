import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  /** Restituisce i prodotti di uno shop */
  getProductInfo(shopId: number): Observable<any> {
    return this.http.get(`/api/shops/${shopId}/products`);
  }
}