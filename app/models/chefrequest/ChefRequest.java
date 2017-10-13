package models.chefrequest;

import models.BaseModel;
import models.Media;
import models.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ChefRequest extends BaseModel {

    private boolean answered;

    @OneToOne(optional = false)
    private User user;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Media media;

    @Column(nullable = false, length = 4096)
    private String resume;

    public ChefRequest() {}

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
