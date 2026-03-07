package com.unicam.cs.progettoweb.marketplace.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Eccezione personalizzata per il marketplace.
 * Contiene un messaggio di errore e uno status HTTP associato.
 */

@Getter
public class MarketplaceException extends RuntimeException{

    private final HttpStatus status;

    public MarketplaceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
