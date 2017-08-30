package server.exception;

import server.error.RequestError;

import javax.validation.constraints.NotNull;

/**
 * Exception to be catch by controllers. Controllers are responsible for returning
 * to the client an error response with content type JSON and the exception message
 * as body.
 */
public class BadRequestException extends Exception {

    public BadRequestException(@NotNull RequestError error) {
        super(error.toString());
    }
}
