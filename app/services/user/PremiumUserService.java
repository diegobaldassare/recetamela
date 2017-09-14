package services.user;

import com.avaje.ebean.Model;
import models.PremiumUser;
import services.Service;

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
