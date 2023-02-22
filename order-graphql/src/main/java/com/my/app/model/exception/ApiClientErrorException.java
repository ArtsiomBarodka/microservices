package com.my.app.model.exception;

public class ApiClientErrorException extends RuntimeException {
    public ApiClientErrorException() {
        super();
    }

    public ApiClientErrorException(String message) {
        super(message);
    }

    public ApiClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiClientErrorException(Throwable cause) {
        super(cause);
    }

    protected ApiClientErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
