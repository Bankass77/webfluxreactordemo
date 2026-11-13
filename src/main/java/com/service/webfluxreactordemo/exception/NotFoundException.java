package com.service.webfluxreactordemo.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String externalServiceError) {
        super(externalServiceError);
    }
}
