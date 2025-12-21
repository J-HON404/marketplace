import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../interfaces/api-response';

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private baseUrl = '/api/profiles';
  constructor(private http: HttpClient) {}

  getProfile(id: number): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/${id}`);
  }
}
