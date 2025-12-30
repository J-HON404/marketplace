import { Injectable } from '@angular/core';

/**
 * Servizio Angular per la gestione del token JWT lato client.
 * Si occupa di:
 * 1. Memorizzare il token in sessionStorage.
 * 2. Recuperare e rimuovere il token.
 * 3. Decodificare il token per estrarre informazioni utili:
 *    - ruolo dell'utente (role)
 *    - ID del profilo (profileId)
 *    - ID del negozio (shopId)
 * Questo servizio viene utilizzato ad esempio dall'interceptor HTTP per aggiungere
 * il token alle richieste e dai componenti per controllare permessi o dati dell'utente.
 */

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'jwt_token';

  setToken(token: string) {
    sessionStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.TOKEN_KEY);
  }

  clearToken() {
    sessionStorage.removeItem(this.TOKEN_KEY);
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role || null;
    } catch {
      return null;
    }
  }

  getProfileId(): number | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.profileId || null;
    } catch {
      return null;
    }
  }

getShopId(): number | null {
  const token = this.getToken();
  if (!token) return null;
  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.shopId ?? null;
}


}
