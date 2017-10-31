package models.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "AdminUser")
public class AdminUser extends ChefUser {

    public AdminUser(String name, String lastName, String email, String profilePic) {
        super(name, lastName, email, profilePic);
    }
}
