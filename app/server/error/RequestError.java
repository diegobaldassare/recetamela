package server.error;

import org.jetbrains.annotations.NotNull;

/**
 * All request errors are declared here and used when necessary.
 */
public enum RequestError {
    BAD_FORMAT(new RequestErrorJson(1, "Bad format").toString()),
    CATEGORY_EXISTS(new RequestErrorJson(1, "Category already exists").toString()),
    CATEGORY_NOT_EXISTS(new RequestErrorJson(1, "Category doesn't exist").toString()),
    EMAIL_TAKEN(new RequestErrorJson(2, "Email already taken").toString());

    private String json;

    RequestError(@NotNull String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return json;
    }
}
