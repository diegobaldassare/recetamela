package services.user;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlUpdate;
import models.user.AdminUser;
import models.user.User;
import services.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdminUserService extends Service<AdminUser> {

    private static AdminUserService instance;

    private AdminUserService(Model.Finder<Long, AdminUser> finder) {
        super(finder);
    }

    public static AdminUserService getInstance() {
        if (instance == null) instance = new AdminUserService(new Model.Finder<>(AdminUser.class));
        return instance;
    }

    public void create(Long userID) {
        UserService.getInstance().get(userID).ifPresent(user -> {
            user.setType("AdminUser");
            user.update();
            SqlUpdate delete = Ebean.createSqlUpdate(
                    "update user " +
                            "set expiration_date = :date " +
                            "where id = :id");
            delete.setParameter("date", new Date());
            delete.setParameter("id", userID);
            Ebean.execute(delete);
        });
    }

    public List<AdminUser> getAdmins() {
        return getFinder().all();
    }

}
