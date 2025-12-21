import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Products } from '../interfaces/product';
import { ApiResponse } from '../interfaces/api-response';

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