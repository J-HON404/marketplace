package com.unicam.cs.progettoweb.marketplace.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MarketplaceException extends RuntimeException{

    private final HttpStatus status;

    public MarketplaceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
