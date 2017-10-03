package models;

import models.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Followers extends BaseModel{

    @ManyToOne(optional = false)
    private User follower;

    @ManyToOne(optional = false)
    private User following;

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "Followers{" +
                "follower=" + follower +
                ", following=" + following +
                '}';
    }
}
