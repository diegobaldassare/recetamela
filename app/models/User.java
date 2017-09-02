package models;

import models.media.Media;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
@Entity
public class User extends BaseModel {

    private String name, lastName;

    @Column(unique=true)
    private String email;

    private Media profilePic;

    public static final Finder<Long, User> find = new Finder<>(User.class);

    public User() {
    }

    public User(String name, String lastName, String email, Media profilePic) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Media getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Media profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
