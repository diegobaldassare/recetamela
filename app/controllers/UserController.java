package controllers;

import com.avaje.ebean.Model;
import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
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
        if (!UserService.getInstance().get(user.getId()).isPresent()) {
            user.save();
            return ok(Json.toJson(user));
        }
        else return notFound();
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
        Optional<User> user = UserService.getInstance().get(id);
        if (user.isPresent()) return ok(Json.toJson(user.get()));
        return notFound();
    }
}
