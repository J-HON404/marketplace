package com.unicam.cs.progettoweb.marketplace.auth.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO per la risposta di autenticazione.
 * Contiene il token JWT generato al login.
 */

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
