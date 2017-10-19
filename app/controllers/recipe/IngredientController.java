package controllers.recipe;

import controllers.authentication.Authenticate;
import models.user.AdminUser;
import models.user.ChefUser;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.recipe.Ingredient;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.recipe.IngredientService;

import java.util.List;

public class IngredientController extends Controller {

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getAll() {
        final List<Ingredient> ingredients = IngredientService.getInstance().getFinder().all();
        return ok(Json.toJson(ingredients));
    }
}
