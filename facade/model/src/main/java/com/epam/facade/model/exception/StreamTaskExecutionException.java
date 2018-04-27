package com.epam.facade.model.exception;

public class StreamTaskExecutionException extends RuntimeException {
    public StreamTaskExecutionException() {
        super();
    }

    public StreamTaskExecutionException(String message) {
        super(message);
    }

    public StreamTaskExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamTaskExecutionException(Throwable cause) {
        super(cause);
    }

    protected StreamTaskExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
