package com.unicam.cs.progettoweb.marketplace.dto.auth;

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
