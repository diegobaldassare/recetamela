package controllers.user;

import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;

public class AdminUserController extends ChefUserController {

    @Inject
    public AdminUserController(FormFactory formFactory) {
        super(formFactory);
    }

    public Result updateAdminUser(Long id) {
        return updateChefUser(id);
    }

    public Result getAdminUser(Long id) {
        return getChefUser(id);
    }
}
