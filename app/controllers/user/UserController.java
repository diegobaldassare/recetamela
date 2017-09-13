package controllers.user;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Optional;

public abstract class UserController extends Controller {

    protected Form<User> userForm;

    @Inject
    public UserController(FormFactory formFactory) {
        userForm =  formFactory.form(User.class);
    }

    public abstract Result createUser();

    public abstract Result updateUser();

    public abstract Result getUser();

    public abstract Result getUsers();

    public abstract Result deleteUser(Long id);
}
