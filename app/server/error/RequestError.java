package server.error;

import org.jetbrains.annotations.NotNull;

/**
 * All request errors are declared here and used when necessary.
 */
public enum RequestError {
    BAD_FORMAT(new RequestErrorJson(1, "Bad format").toString()),
    EMAIL_TAKEN(new RequestErrorJson(2, "Email already taken").toString()),
    CATEGORY_EXISTS(new RequestErrorJson(3, "Category already exists").toString()),
    CATEGORY_NOT_EXISTS(new RequestErrorJson(4, "Category doesn't exist").toString());

    private String json;

    RequestError(@NotNull String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return json;
    }
}
