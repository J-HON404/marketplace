import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs'; // <-- importa 'of'
import { TokenService } from './token.service'; // <-- importa TokenService
import { Profile, UserRole } from '../../models/interfaces/profile';
import { ApiResponse } from '../../models/interfaces/api-response';

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

  constructor(
    private http: HttpClient,
    private tokenService: TokenService // <-- inietta TokenService
  ) {}

  register(payload: RegisterPayload): Observable<ApiResponse<Profile>> {
    return this.http.post<ApiResponse<Profile>>(`${this.baseUrl}/register`, payload);
  }

  login(username: string, password: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.baseUrl}/login`, { username, password });
  }

  logout(): Observable<ApiResponse<void>> {
    const token = this.tokenService.getToken();
    if (!token) {
      return of({ success: true, message: 'No token to logout', data: undefined } as ApiResponse<void>);
    }

    return this.http.post<ApiResponse<void>>(
      `${this.baseUrl}/logout`,
      {},
      { headers: { Authorization: `Bearer ${token}` } }
    );
  }
}
