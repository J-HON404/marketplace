package com.unicam.cs.progettoweb.marketplace.dto.auth;

import lombok.Data;

/**
 * DTO per la richiesta di login.
 */

@Data
public class LoginRequest {
    private String username;
    private String password;
}
