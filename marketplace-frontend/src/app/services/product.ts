import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Products } from '../interfaces/products';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }


  getProductInfo(shopId: number): Observable<any> {
    return this.http.get(`/api/shops/${shopId}/products`);
  }

  createProduct(shopId: number, product: Products): Observable<Products> {
  return this.http.post<Products>(`/api/shops/${shopId}/products`, product);
}

  updateProduct(shopId: number, productId: number, product: Products): Observable<any>{
    return this.http.put(`/api/shops/${shopId}/products/${productId}`,product);
  }

  deleteProduct(shopId: number, productId: number): Observable<any> {
  return this.http.delete(`/api/shops/${shopId}/products/${productId}`);
}

}