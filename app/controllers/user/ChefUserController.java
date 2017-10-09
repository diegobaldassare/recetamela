package controllers.user;

import controllers.BaseController;
import models.user.ChefUser;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.user.ChefUserService;

import javax.inject.Inject;
import java.util.Optional;

public class ChefUserController extends BaseController {

    private static Form<ChefUser> userForm;

    @Inject
    public ChefUserController(FormFactory formFactory) {
        userForm = formFactory.form(ChefUser.class);
    }

    public Result updateChefUser(Long id) {
        ChefUser newChefUser = userForm.bindFromRequest().get();
        return ChefUserService.getInstance().get(id).map(user -> {
            if (newChefUser.getName() != null) user.setName(newChefUser.getName());
            if (newChefUser.getLastName() != null) user.setLastName(newChefUser.getLastName());
            if (newChefUser.getEmail() != null) user.setEmail(newChefUser.getEmail());
            if (newChefUser.getProfilePic() != null) user.setProfilePic(newChefUser.getProfilePic());
            if (newChefUser.getAuthToken() != null) user.setAuthToken(newChefUser.getAuthToken());
            if (newChefUser.getRecipes() != null) user.setRecipes(newChefUser.getRecipes());
            if (newChefUser.getRecipebooks() != null) user.setRecipebooks(newChefUser.getRecipebooks());
            if (newChefUser.getExpirationDate() != null) user.setExpirationDate(newChefUser.getExpirationDate());
            if (newChefUser.getNews() != null) user.setNews(newChefUser.getNews());
            if (newChefUser.getFollowedCategories() != null) user.setFollowedCategories(newChefUser.getFollowedCategories());
            user.setFacebookId(newChefUser.getFacebookId());
            user.update();
            return ok(Json.toJson(user));
        }).orElse(notFound());
    }

    public Result getChefUser(Long id) {
        final Optional<ChefUser> user = ChefUserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElse(notFound());
    }

    public Result getChefUsers() {
        return ok(Json.toJson(ChefUserService.getInstance().getFinder().all()));
    }

    public Result deleteChefUser(Long id) {
        return ChefUserService.getInstance().get(id).map(user -> {
            user.delete();
            return ok();
        }).orElse(notFound());
    }
}
