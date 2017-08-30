package controllers;

import models.recipe.Recipe;
import play.mvc.Controller;
import play.mvc.Result;

public class RecipeController extends Controller {

    public Result createRecipe(Recipe recipe) {
        return ok(recipe.toString());
    }
}
