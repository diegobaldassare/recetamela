package server.error;

import org.jetbrains.annotations.NotNull;

/**
 * All request errors are declared here and used when necessary.
 */
public enum RequestError {
    BAD_FORMAT(new RequestErrorJson(1, "Bad format").toString());

    private final String json;

    RequestError(@NotNull String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return json;
    }
}
