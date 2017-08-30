package server.error;

/**
 * All client errors are defined here and returned when necessary.
 * They are stored in memory as string JSON representations.
 */
public interface Errors {
    String BAD_FORMAT = new Error(1, "Bad format").toString();
    String EMAIL_TAKEN = new Error(2, "Email already taken").toString();
}
