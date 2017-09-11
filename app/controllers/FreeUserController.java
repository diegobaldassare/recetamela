package controllers;

import com.google.inject.Inject;
import models.FreeUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.FreeUserService;

import java.util.List;
import java.util.Optional;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class FreeUserController extends Controller {

    private static Form<User> userForm;

    @Inject
    public FreeUserController(FormFactory formFactory) {
        userForm =  formFactory.form(User.class);
    }

    public Result createFreeUser() {
        User user = userForm.bindFromRequest().get();
        user.save();
        return ok(Json.toJson(user));
    }

    public Result getFreeUsers(){
        List<FreeUser> users = FreeUserService.getInstance().getFinder().all();
        return ok(Json.toJson(users));
    }

    public Result deleteFreeUser(Long id){
        Optional<FreeUser> user = FreeUserService.getInstance().get(id);
        if (user.isPresent()){
            user.get().delete();
            return ok();
        }
        return notFound();
    }

    public Result getFreeUser(Long id) {
        final Optional<FreeUser> user = FreeUserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElseGet(Results::notFound);
    }
}
