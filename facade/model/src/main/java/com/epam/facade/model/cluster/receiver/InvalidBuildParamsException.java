package com.epam.facade.model.cluster.receiver;

public class InvalidBuildParamsException extends Exception {
    public InvalidBuildParamsException() {
        super();
    }

    public InvalidBuildParamsException(String message) {
        super(message);
    }

    public InvalidBuildParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBuildParamsException(Throwable cause) {
        super(cause);
    }

    protected InvalidBuildParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
