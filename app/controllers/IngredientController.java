package controllers;

import models.recipe.Ingredient;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.recipe.IngredientService;

import java.util.List;


class IngredientController extends Controller {

    // @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getAll() {
        final List<Ingredient> ingredients = IngredientService.getInstance().getFinder().all();
        return ok(Json.toJson(ingredients));
    }
}
