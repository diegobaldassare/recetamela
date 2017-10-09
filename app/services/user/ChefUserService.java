package services.user;

import com.avaje.ebean.Model;
import models.user.ChefUser;
import services.Service;

public class ChefUserService extends Service<ChefUser> {

    private static ChefUserService instance;

    private ChefUserService(Model.Finder<Long, ChefUser> finder) {
        super(finder);
    }

    public static ChefUserService getInstance() {
        if (instance == null) instance = new ChefUserService(new Model.Finder<>(ChefUser.class));
        return instance;
    }
}
