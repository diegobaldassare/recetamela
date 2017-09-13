package services;

import com.avaje.ebean.Model;
import models.FreeUser;
import models.User;

import java.util.Optional;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class FreeUserService extends Service<FreeUser> {

    private static FreeUserService instance;

    private FreeUserService(Model.Finder<Long, FreeUser> finder) {
        super(finder);
    }

    public static FreeUserService getInstance() {
        if (instance == null)
            instance = new FreeUserService(new Model.Finder<>(FreeUser.class));
        return instance;
    }

    public Optional<FreeUser> findByFacebookId(Long id) {
        return Optional.ofNullable(getFinder().where().eq("facebook_id", id).findUnique());
    }

    public Optional<FreeUser> findByAuthToken(String authToken) {
        return Optional.ofNullable(getFinder().where().eq("auth_token", authToken).findUnique());
    }
}
