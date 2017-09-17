package models.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "FreeUser")
public class FreeUser extends User {

    public FreeUser() {}

    public FreeUser(String name, String lastName, String email, String profilePic) {
        super(name, lastName, email, profilePic);
    }
}
