package com.unicam.cs.progettoweb.marketplace.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MarketplaceException.class)
    public ProblemDetail handleBusinessException(MarketplaceException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(ex.getStatus());
        problem.setTitle(ex.getStatus().getReasonPhrase());
        problem.setDetail(ex.getMessage());
        return problem;
    }
}
