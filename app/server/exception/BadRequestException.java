package server.exception;

import org.jetbrains.annotations.NotNull;
import server.error.RequestError;

/**
 * Exception to be catch by controllers. Controllers are responsible for returning
 * to the client an error response with content type JSON and the exception message
 * as body.
 */
public class BadRequestException extends Exception {

    public BadRequestException(@NotNull RequestError requestError) {
        super(requestError.toString());
    }
}
