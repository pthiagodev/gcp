package com.pthiago.gcp.api.domain.exception;

public class NotificationFailureException extends RuntimeException {
    public NotificationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
