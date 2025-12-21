import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserRole } from '../../interfaces/profile'; 
import { ApiResponse } from '../../interfaces/api-response'; // Importa l'interfaccia

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

  // Tipizziamo come ApiResponse<any> o <void> per la registrazione
  register(payload: RegisterPayload): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/register`, payload);
  }

  // Tipizziamo come ApiResponse<string> perché il data conterrà il token (stringa)
  login(username: string, password: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.baseUrl}/login`, { username, password });
  }
}