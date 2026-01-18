import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/interfaces/api-response';
import { Profile } from '../models/interfaces/profile';

/**
 * ProfileService è un servizio che gestisce le operazioni HTTP relative ai profili
 * Funzionalità principali:
 * - Recuperare i dati di un profilo tramite l'ID dell'utente.
 */
@Injectable({ providedIn: 'root' })
export class ProfileService {
  private baseUrl = '/api/profiles';

  constructor(private http: HttpClient) {}

  getProfile(id: number): Observable<ApiResponse<Profile>> {
    return this.http.get<ApiResponse<Profile>>(`${this.baseUrl}/${id}`);
  }
}
