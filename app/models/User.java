package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.payment.CreditCard;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class User extends BaseModel {

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    private long facebookId;

    private String authToken;

    private String name, lastName;

    @Column(unique=true)
    private String email;

    private String profilePic;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<CreditCard> creditCards;

    public User() {}

    public User(String name, String lastName, String email, String profilePic) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
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

    public String getType() {
        return type;
    }

    public Set<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(Set<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }
}
