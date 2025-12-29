package com.unicam.cs.progettoweb.marketplace.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper generico per le risposte API.
 * Contiene un messaggio opzionale e i dati della risposta.
 * @param <T> Tipo dei dati contenuti nella risposta
 */

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }
}
