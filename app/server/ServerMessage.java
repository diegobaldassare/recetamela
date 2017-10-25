package server;

/**
 * Created by Matias Cicilia on 25-Oct-17.
 */
public class ServerMessage<T> {
    private String header;
    private T object;

    public ServerMessage(String header, T object) {
        this.header = header;
        this.object = object;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
