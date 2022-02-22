package com.bugtracker.user.exception;

public class IncorrectCredentials extends RuntimeException {

    public IncorrectCredentials() {
    }

    public IncorrectCredentials(String message) {
        super(message);
    }

    public IncorrectCredentials(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectCredentials(Throwable cause) {
        super(cause);
    }

    public IncorrectCredentials(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
