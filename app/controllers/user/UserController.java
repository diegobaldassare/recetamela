package controllers.user;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.recipe.RecipeCategory;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.exception.BadRequestException;
import services.recipe.RecipeCategoryService;
import services.recipe.RecipeValidator;
import services.user.UserService;
import services.user.UserValidator;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return UserService.getInstance().get(id)
                .map(user -> ok(Json.toJson(user.getFollowedCategories())))
                .orElseGet(Results::notFound);
    }

    public Result getUnFollowedCategories(String id) {
        Optional<User> me = UserService.getInstance().get(Long.parseLong(id));
        if (!me.isPresent()) return notFound();
        final List<RecipeCategory> allCategories = RecipeCategoryService.getInstance().getFinder().all();
        final List<RecipeCategory> unFollowed = allCategories.stream()
                .filter(c -> !me.get().getFollowedCategories().contains(c))
                .collect(Collectors.toList());
        return ok(Json.toJson(unFollowed));
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result subscribeToCategory(long id) {
        User me = getRequester();
        Optional<RecipeCategory> category = RecipeCategoryService.getInstance().get(id);

        if (!category.isPresent()) return notFound();
        me.getFollowedCategories().add(category.get());
        category.get().getFollowers().add(me);
        category.get().update();
        me.update();

        return ok(Json.toJson(me));
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result unSubscribeToCategory(long id) {
        User me = getRequester();
        Optional<RecipeCategory> category = RecipeCategoryService.getInstance().get(id);

        if (!category.isPresent()) return notFound();
        me.getFollowedCategories().remove(category.get());
        category.get().getFollowers().remove(me);
        category.get().update();
        me.update();

        return ok(Json.toJson(category.get()));
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result modify(long id) {
        final Optional<User> user = UserService.getInstance().get(id);
        if (!user.isPresent()) return notFound();
        final User u = getBody(User.class);
        try {
            UserValidator.validateNotNullFields(u);
            UserService.getInstance().modify(user.get(), u);
            user.get().save();
            return ok(Json.toJson(user.get()));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }
}
