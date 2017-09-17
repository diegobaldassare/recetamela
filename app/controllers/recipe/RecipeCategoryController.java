package controllers.recipe;

import controllers.authentication.Authenticate;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.recipe.RecipeCategory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.recipe.RecipeCategoryService;

import java.util.List;

public class RecipeCategoryController extends Controller {

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getAll() {
        final List<RecipeCategory> categories = RecipeCategoryService.getInstance().getFinder().all();
        return ok(Json.toJson(categories));
    }
}
