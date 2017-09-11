package services;

import com.avaje.ebean.Model;
import models.PremiumUser;

public class PremiumUserService extends Service<PremiumUser> {

    private static PremiumUserService instance;

    private PremiumUserService(Model.Finder<Long, PremiumUser> finder) {
        super(finder);
    }

    public static PremiumUserService getInstance() {
        if (instance == null)
            instance = new PremiumUserService(new Model.Finder<>(PremiumUser.class));
        return instance;
    }
}
