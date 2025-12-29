package com.unicam.cs.progettoweb.marketplace.dto.auth;

import com.unicam.cs.progettoweb.marketplace.model.enums.ProfileRole;
import lombok.Data;

/**
 * DTO per la richiesta di registrazione di un nuovo profilo.
 */
@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String address;
    private ProfileRole role;
}
