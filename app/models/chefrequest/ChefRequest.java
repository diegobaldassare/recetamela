package models.chefrequest;

import models.BaseModel;
import models.user.User;

import javax.persistence.Entity;

@Entity
public class ChefRequest extends BaseModel {

    private boolean ansewered;

    private User user;

    public boolean isAnsewered() {
        return ansewered;
    }

    public void setAnsewered(boolean ansewered) {
        this.ansewered = ansewered;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
