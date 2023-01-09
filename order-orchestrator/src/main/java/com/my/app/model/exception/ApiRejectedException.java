package com.my.app.model.exception;

public class ApiRejectedException extends RuntimeException {
    public ApiRejectedException() {
        super();
    }

    public ApiRejectedException(String message) {
        super(message);
    }

    public ApiRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiRejectedException(Throwable cause) {
        super(cause);
    }

    protected ApiRejectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
