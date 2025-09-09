package com.example.exception;

public class NonRetryableException extends BusinessException {
    public NonRetryableException(String message) {
        super(message);
    }
}