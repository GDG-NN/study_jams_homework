package com.gdgnn.filatov.kriya.exception;

public class MessageNotFoundException extends Exception {

    public MessageNotFoundException() {
        super();
    }

    public MessageNotFoundException(final String message) {
        super(message);
    }

    public MessageNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MessageNotFoundException(final Throwable cause) {
        super(cause);
    }
}
