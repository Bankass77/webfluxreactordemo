package com.service.webfluxreactordemo.exception;

import java.time.Instant;

public record ApiErrorResponse(int status, String message, Instant timestamp) {
}
