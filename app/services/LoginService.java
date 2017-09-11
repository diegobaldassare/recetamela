package services;

import com.avaje.ebean.Model;
import models.AuthToken;
import models.User;

import java.util.Optional;

/**
 * Created by Matias Cicilia on 10-Sep-17.
 */
public class LoginService extends Service<AuthToken> {

    private LoginService(Model.Finder<Long, AuthToken> finder) {
        super(finder);
    }

    private static LoginService instance;

    public static LoginService getInstance() {
        if (instance == null)
            instance = new LoginService(new Model.Finder<>(AuthToken.class));
        return instance;
    }

    public Optional<AuthToken> findByHash(String hash) {
        return Optional.ofNullable(getFinder().where().eq("token", hash).findUnique());
    }
}
