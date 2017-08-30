package server.error;

/**
 * All request errors are defined here and returned when necessary.
 */
public interface RequestErrors {
    RequestError BAD_FORMAT = new RequestError(1, "Bad format");
    RequestError EMAIL_TAKEN = new RequestError(2, "Email already taken");
}
