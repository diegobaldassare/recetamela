package models.user;

public class CheckExpirationDateResponse {
    public User user;
    public boolean expired;

    public CheckExpirationDateResponse(User user, boolean expired) {
        this.user = user;
        this.expired = expired;
    }
}
