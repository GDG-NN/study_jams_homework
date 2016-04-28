package com.gdgnn.filatov.kriya.exception;

public class NetworkException extends Exception {

    public NetworkException() {
        super();
    }

    public NetworkException(final String message) {
        super(message);
    }

    public NetworkException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetworkException(final Throwable cause) {
        super(cause);
    }
}