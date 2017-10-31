package server;

import models.user.AdminUser;
import models.user.User;

/**
 * Created by Diego Baldassare on 30/10/2017.
 */
public final class RecetamelaUser {

    private final static String name = "Recetame";
    private final static String lastName = "la receta";
    private final static String email = "recetamelareceta@gmail.com";
    private final static String profilePic = "https://i.imgur.com/bMGr7dT.png";

    public static User getUser() {
        return new AdminUser(name, lastName, email, profilePic);
    }
}
