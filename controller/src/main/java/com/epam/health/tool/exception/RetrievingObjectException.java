package com.epam.health.tool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.NOT_FOUND )
public class RetrievingObjectException extends RuntimeException {
    public RetrievingObjectException() {
        super();
    }

    public RetrievingObjectException(String message) {
        super(message);
    }

    public RetrievingObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetrievingObjectException(Throwable cause) {
        super(cause);
    }

    protected RetrievingObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
