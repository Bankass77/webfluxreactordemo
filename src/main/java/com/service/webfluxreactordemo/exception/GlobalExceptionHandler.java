package com.service.webfluxreactordemo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific execption types
    // Return a Mono just like regular endpoints
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiErrorResponse> handleNotFoundException(NotFoundException e) {

        return Mono.just(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), Instant.now()));
    }

    // catch-all for unpexpected exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiErrorResponse> handleException(Exception e) {
        return Mono.just(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred : " + e.getMessage(), Instant.now()));
    }
}
