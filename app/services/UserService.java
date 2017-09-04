package services;

import com.avaje.ebean.Model;
import models.User;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class UserService extends Service<User> {

    private static UserService instance;

    private UserService(Model.Finder<Long, User> finder) {
        super(finder);
    }

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService(new Model.Finder<>(User.class));
        return instance;
    }
}
