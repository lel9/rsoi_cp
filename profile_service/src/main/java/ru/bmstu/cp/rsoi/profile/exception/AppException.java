package ru.bmstu.cp.rsoi.profile.exception;

public class AppException extends RuntimeException {

    public AppException() {
        super();
    }

    public AppException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AppException(final String message) {
        super(message);
    }

    public AppException(final Throwable cause) {
        super(cause);
    }

}
