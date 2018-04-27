package com.epam.facade.model.cluster.receiver;

public class InvalidBuildParamsRuntimeException extends RuntimeException {
    public InvalidBuildParamsRuntimeException() {
        super();
    }

    public InvalidBuildParamsRuntimeException(String message) {
        super(message);
    }

    public InvalidBuildParamsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBuildParamsRuntimeException(Throwable cause) {
        super(cause);
    }

    protected InvalidBuildParamsRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
