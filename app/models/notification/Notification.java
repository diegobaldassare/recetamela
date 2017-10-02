package models.notification;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.BaseModel;
import models.user.User;
import play.libs.EventSource;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;


/**
 * Created by Matias Cicilia on 21-Sep-17.
 */
@Entity
public class Notification extends BaseModel {

    private long sender;

    private String senderName;

    private String senderPic;

    private Long receiver;

    private String title;

    private boolean delivered;

    private Date time;

    public Notification(long sender, String senderName, Long receiverId, String senderPic, NotificationType title) {
        this.sender = sender;
        this.senderName = senderName;
        this.senderPic = senderPic;
        this.receiver = receiverId;
        this.title = title.name();
        this.time = new Date(System.currentTimeMillis());
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public EventSource.Event toEvent() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(this);
        return EventSource.Event.event(node).withName(this.title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(NotificationType title) {
        this.title = title.name();
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPic() {
        return senderPic;
    }

    public void setSenderPic(String senderPic) {
        this.senderPic = senderPic;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }
}
