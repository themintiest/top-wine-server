package vn.topwines.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable clause) {
        super(message, clause);
    }
}
