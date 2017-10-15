package services.user;

import com.avaje.ebean.Model;
import models.user.AdminUser;
import services.Service;

public class AdminUserService extends Service<AdminUser> {

    private static AdminUserService instance;

    private AdminUserService(Model.Finder<Long, AdminUser> finder) {
        super(finder);
    }

    public static AdminUserService getInstance() {
        if (instance == null) instance = new AdminUserService(new Model.Finder<>(AdminUser.class));
        return instance;
    }

}
