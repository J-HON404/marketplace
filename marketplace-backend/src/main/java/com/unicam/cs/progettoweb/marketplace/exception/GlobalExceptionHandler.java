package com.unicam.cs.progettoweb.marketplace.exception;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestore globale delle eccezioni per le API.
 * Intercetta le MarketplaceException e restituisce una risposta API standardizzata.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MarketplaceException.class)
    public ResponseEntity<ApiResponse<Object>> handleMarketplaceException(MarketplaceException ex) {
        ApiResponse<Object> message = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(message);
    }
}
