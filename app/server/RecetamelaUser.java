package server;

import models.user.AdminUser;
import models.user.User;
import services.user.UserService;

import java.time.LocalDate;
import java.time.Period;

/**
 * Created by Diego Baldassare on 30/10/2017.
 */
public final class RecetamelaUser {

    private final static Long facebookId = 1337L;
    private final static String name = "Recetame";
    private final static String lastName = "la Receta";
    private final static String email = "recetamelareceta@gmail.com";
    private final static String profilePic = "https://i.imgur.com/bMGr7dT.png";

    public static User getUser() {
        if (UserService.getInstance().findByFacebookId(facebookId).isPresent()) {
            return UserService.getInstance().findByFacebookId(facebookId).get();
        }
        else {
            final AdminUser user = new AdminUser(name, lastName, email, profilePic);
            user.setFacebookId(facebookId);
            user.setExpirationDate(LocalDate.now().plus(Period.ofMonths(1)));
            user.save();
            return user;
        }
    }
}
