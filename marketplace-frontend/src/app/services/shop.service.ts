import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Shop } from '../models/interfaces/shops';
import { ApiResponse } from '../models/interfaces/api-response';

/**
 * ShopService è un servizio che gestisce le operazioni HTTP relative ai negozi (Shop) dei profili
 * Funzionalità principali:
 * - Creare un nuovo negozio per un profilo.
 * - Aggiornare i dati di un negozio esistente.
 * - Eliminare un negozio associato a un profilo.
 * - Recuperare i dati di un singolo negozio di un profilo.
 * - Recuperare tutti i negozi di un profilo.
 * - Verificare se un profilo è il proprietario di un negozio.
 */

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  private readonly baseUrl = '/api/profiles';

  constructor(private http: HttpClient) { }

  createShop(profileId: number, shop: Partial<Shop>): Observable<ApiResponse<Shop>> {
    return this.http.post<ApiResponse<Shop>>(`${this.baseUrl}/${profileId}/shop`, shop);
  }

  updateShop(profileId: number, shopId: number, shop: Partial<Shop>): Observable<ApiResponse<Shop>> {
    return this.http.put<ApiResponse<Shop>>(`${this.baseUrl}/${profileId}/shop/${shopId}`, shop);
  }

  deleteShop(profileId: number, shopId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${profileId}/shop/${shopId}`);
  }

  getShop(profileId: number): Observable<ApiResponse<Shop>> {
    return this.http.get<ApiResponse<Shop>>(`${this.baseUrl}/${profileId}/shop`);
  }

  getAllShops(profileId: number): Observable<ApiResponse<Shop[]>> {
    return this.http.get<ApiResponse<Shop[]>>(`${this.baseUrl}/${profileId}/shops`);
  }

  checkOwnership(profileId: number, shopId: number): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.baseUrl}/${profileId}/shops/${shopId}/verify-owner`);
  }
}