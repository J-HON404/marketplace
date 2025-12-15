import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserRole } from './user-role.enum';

export interface RegisterPayload {
  username: string;
  password: string;
  email: string;
  address: string;
  role: UserRole;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { username, password });
  }

  register(payload: RegisterPayload): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, payload);
  }
}
