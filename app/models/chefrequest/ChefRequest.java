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

    private boolean accepted;

    @OneToOne(optional = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Media certificate;

    @Column(nullable = false, length = 4096)
    private String resume;

    public ChefRequest() {}

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Media getCertificate() {
        return certificate;
    }

    public void setCertificate(Media certificate) {
        this.certificate = certificate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Media getMedia() {
        return certificate;
    }

    public void setMedia(Media certificate) {
        this.certificate = certificate;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
