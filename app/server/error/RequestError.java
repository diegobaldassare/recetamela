package server.error;

import play.libs.Json;

/**
 * Request error JSON representation.
 */
public class RequestError {
    private long id;
    private String message;

    public RequestError(long id, String message) {
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
