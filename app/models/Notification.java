package models;
import models.user.User;
import play.libs.EventSource;

import javax.persistence.Entity;


/**
 * Created by Matias Cicilia on 21-Sep-17.
 */
@Entity
public class Notification extends BaseModel {
    private Long userId;
    private String title;
    private String message;
    private boolean delivered;

    public Notification(Long userId, String title, String message) {
        this.userId = userId;
        this.title = title;
        this.message = message;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public EventSource.Event toEvent() {
        return new EventSource.Event(message, userId.toString(), title);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", delivered=" + delivered +
                '}';
    }
}
