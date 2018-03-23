package com.epam.health.tool.facade.exception;

public class InvalidResponseException extends Exception {
    public InvalidResponseException() {
        super();
    }

    public InvalidResponseException(String message) {
        super(message);
    }

    public InvalidResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResponseException(Throwable cause) {
        super(cause);
    }

    protected InvalidResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
