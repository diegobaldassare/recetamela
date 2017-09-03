package server.error;

import play.libs.Json;

/**
 * Request error JSON representation.
 */
class RequestErrorJson {
    private final long id;
    private final String message;

    RequestErrorJson(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return Json.stringify(Json.toJson(this));
    }
}
