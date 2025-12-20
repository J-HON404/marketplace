import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  constructor(private http: HttpClient) { }

  /** Restituisce lo shop associato a un profilo */
  getShop(profileId: number): Observable<any> {
    return this.http.get(`/api/profiles/${profileId}/shop`);
  }

  /** Restituisce tutti gli shop visibili per un profilo (authorization) */
  getAllShops(profileId: number): Observable<any> {
    return this.http.get(`/api/profiles/${profileId}/shops`);
  }

  checkOwnership(profileId: number, shopId: number): Observable<any> {
    return this.http.get<any>(`/api/profiles/${profileId}/shops/${shopId}/verify-owner`);
  }
}
