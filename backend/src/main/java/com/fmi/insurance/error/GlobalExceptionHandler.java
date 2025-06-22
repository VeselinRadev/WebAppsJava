package com.fmi.insurance.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TestException.class)
    public ResponseEntity<String> handle(TestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}