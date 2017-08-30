package server.error;

import play.libs.Json;

/**
 * Client error JSON representation.
 */
public class Error {
    private long id;
    private String message;

    public Error(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return Json.stringify(Json.toJson(this));
    }
}
