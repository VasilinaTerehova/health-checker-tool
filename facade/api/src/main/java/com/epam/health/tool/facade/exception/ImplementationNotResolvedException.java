package com.epam.health.tool.facade.exception;

public class ImplementationNotResolvedException extends Exception {
    public ImplementationNotResolvedException() {
        super();
    }

    public ImplementationNotResolvedException(String message) {
        super(message);
    }

    public ImplementationNotResolvedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImplementationNotResolvedException(Throwable cause) {
        super(cause);
    }

    protected ImplementationNotResolvedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
