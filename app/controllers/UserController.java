package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class UserController extends Controller {

    private static Form<User> userForm;

    @Inject
    public UserController(FormFactory formFactory) {
        userForm =  formFactory.form(User.class);
    }

    public Result createUser() {
        User user = userForm.bindFromRequest().get();
        user.save();
        return ok(Json.toJson(user));
    }

    public Result getUsers(){
        List<User> users = UserService.getInstance().getFinder().all();
        return ok(Json.toJson(users));
    }

    public Result deleteUser(Long id){
        Optional<User> user = UserService.getInstance().get(id);
        if (user.isPresent()){
            user.get().delete();
            return ok();
        }
        return notFound();
    }

    public Result getUser(Long id) {
        final Optional<User> user = UserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElseGet(Results::notFound);
    }
}
