package com.service.webfluxreactordemo.exception;

public class ServiceExecption extends RuntimeException {
    public ServiceExecption(String externalServiceError) {
        super(externalServiceError);
    }
}
