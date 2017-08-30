package server.exception;

/**
 * Exception to be catch by controllers. They are responsible for returning
 * to the client an error response. If the exception message is not null
 * it must be returned as the body of the response with JSON content type.
 */
public class BadRequestException extends Exception {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
