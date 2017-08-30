package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.recipe.json.RecipeInputJson;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class RecipeController extends Controller {

    public Result createRecipe() {
        final JsonNode body = request().body().asJson();
        final RecipeInputJson recipeInput = Json.fromJson(body, RecipeInputJson.class);
        return ok();
    }
}
