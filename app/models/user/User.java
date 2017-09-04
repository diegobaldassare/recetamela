package models.user;

import models.BaseModel;

import javax.persistence.Entity;

/**
 * Created by diegobaldassare on 28/08/2017.
 */
@Entity
public class User extends BaseModel {

    private String name;
    private String surname;
    private String profilePicture;
    private String email;
    private long facebookId;

    public User() {
    }

    public User(String name, String surname, String profilePicture, String email, long facebookId) {
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.email = email;
        this.facebookId = facebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }
}
