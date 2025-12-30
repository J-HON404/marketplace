import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserRole } from '../../interfaces/profile'; 
import { ApiResponse } from '../../interfaces/api-response'; // Importa l'interfaccia

/**
 * Servizio di autenticazione per Angular.
 * Fornisce metodi per registrare un nuovo utente e per effettuare il login.
 * Comunica con il backend tramite HttpClient sulle API di autenticazione.
 * - `register(payload: RegisterPayload)`: invia i dati dell'utente al backend per creare un nuovo account.
 * - `login(username, password)`: invia le credenziali al backend per ottenere un token JWT.
 * Le risposte del backend sono tipizzate tramite l'interfaccia `ApiResponse`.
 */

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

  register(payload: RegisterPayload): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/register`, payload);
  }


  login(username: string, password: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.baseUrl}/login`, { username, password });
  }
}