package models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.BaseModel;
import models.payment.CreditCard;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class User extends BaseModel {

    @Column(name = "type", insertable = false)
    private String type;

    private long facebookId;

    private String authToken;

    private String name, lastName;

    @Column(unique=true)
    private String email;

    private String profilePic;

//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<CreditCard> creditCards;

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

    public void setName(String name) {
        this.name = name;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
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

    public void setType(String type) {
        this.type = type;
    }

//    public List<CreditCard> getCreditCards() {
//        return creditCards;
//    }
//
//    public void setCreditCards(List<CreditCard> creditCards) {
//        this.creditCards = creditCards;
//    }
}
