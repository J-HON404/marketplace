import { Injectable } from '@angular/core';

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
