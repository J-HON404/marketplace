/**
 * HTTP Interceptor che aggiunge automaticamente il token JWT
 * all'header Authorization di tutte le richieste HTTP in uscita.
 * Se un token è presente nel TokenService, la richiesta viene clonata
 * e arricchita con l'header "Authorization: Bearer <token>".
 * In questo modo il backend può autenticare l'utente ad ogni chiamata.
 */

import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const token = tokenService.getToken();

  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  return next(req);
};