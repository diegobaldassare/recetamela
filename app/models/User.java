package models;

import javax.persistence.*;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_Type")
public abstract class User extends BaseModel {

    private long facebookId;

    private String authToken;

    private String name, lastName;
    @Column(unique=true)
    private String email;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Media profilePic;

    public User() {}

    public User(String name, String lastName, String email, Media profilePic) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
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

    public String  getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String  authToken) {
        this.authToken = authToken;
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
