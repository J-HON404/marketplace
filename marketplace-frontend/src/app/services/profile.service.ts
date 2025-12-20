import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from '../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private baseUrl = '/api/profiles';

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  getProfile(id: number): Observable<any> {
    const token = this.tokenService.getToken();
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    return this.http.get<any>(`${this.baseUrl}/${id}`, { headers });
  }
}
