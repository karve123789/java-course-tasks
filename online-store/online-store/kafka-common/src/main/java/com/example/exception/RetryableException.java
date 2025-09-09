package com.example.exception;

public class RetryableException extends BusinessException {
    public RetryableException(String message) {
        super(message);
    }
}