package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}