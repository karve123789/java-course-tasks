package com.example.company.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errorResponse = Map.of(
                "error", ex.getMessage(),
                "status", String.valueOf(HttpStatus.NOT_FOUND.value())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}