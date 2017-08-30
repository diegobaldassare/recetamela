package server.exception;

import server.error.RequestError;

import javax.validation.constraints.NotNull;

/**
 * Exception to be catch by controllers. Controllers are responsible for returning
 * to the client an error response with content type JSON and the request error
 * as body.
 */
public class BadRequestException extends Exception {

    private final RequestError requestError;

    public BadRequestException(@NotNull RequestError requestError) {
        super();
        this.requestError = requestError;
    }

    public RequestError getRequestError() {
        return requestError;
    }
}
