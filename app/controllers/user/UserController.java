package controllers.user;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.notification.NotificationType;
import models.recipe.RecipeCategory;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.user.FreeUserService;
import services.user.UserService;
import util.NotificationManager;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public class UserController extends BaseController {

    protected Form<User> userForm;

    @Inject
    public UserController(FormFactory formFactory) {
        userForm =  formFactory.form(User.class);
    }

    public Result createUser() {
        User user = userForm.bindFromRequest().get();
        user.save();
        return ok(Json.toJson(user));
    }

    public Result updateUser(Long id) {
        try {
            return updateUser(id, user -> {
                user.update();
                return ok(Json.toJson(user));
            });
        } catch (NoSuchObjectException err) {
            return notFound(err.getMessage());
        }
    }

    public Result getUser(Long id) {
        final Optional<User> user = UserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElseGet(Results::notFound);
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

    private Result updateUser(Long id, Function<User, Result> function) throws NoSuchObjectException {
        User newFreeUser = userForm.bindFromRequest().get();
        Optional<User> userOptional = UserService.getInstance().get(id);
        User oldFreeUser;
        if(userOptional.isPresent()) oldFreeUser = userOptional.get();
        else throw new NoSuchObjectException("The user was not found");
        if (newFreeUser.getName() != null) oldFreeUser.setName(newFreeUser.getName());
        if (newFreeUser.getLastName() != null) oldFreeUser.setLastName(newFreeUser.getLastName());
        if (newFreeUser.getEmail() != null) oldFreeUser.setEmail(newFreeUser.getEmail());
        if (newFreeUser.getProfilePic() != null) oldFreeUser.setProfilePic(newFreeUser.getProfilePic());
        if (newFreeUser.getAuthToken() != null) oldFreeUser.setAuthToken(newFreeUser.getAuthToken());
        oldFreeUser.setFacebookId(newFreeUser.getFacebookId());
        return function.apply(oldFreeUser);
    }

    public Result getRecipeCategories(Long id) {
        return UserService.getInstance().get(id).map(user -> ok(Json.toJson(user.getFollowedCategories()))).orElseGet(Results::notFound);
    }
}
