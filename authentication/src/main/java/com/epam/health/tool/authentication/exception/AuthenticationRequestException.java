package com.epam.health.tool.authentication.exception;

public class AuthenticationRequestException extends Exception {
    public AuthenticationRequestException() {
    }

    public AuthenticationRequestException(String message) {
        super(message);
    }

    public AuthenticationRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationRequestException(Throwable cause) {
        super(cause);
    }

    public AuthenticationRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
