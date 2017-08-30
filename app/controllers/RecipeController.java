package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.recipe.Recipe;
import models.recipe.json.RecipeInputJson;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import server.exception.BadRequestException;

public class RecipeController extends Controller {

    public Result createRecipe() {
        final JsonNode body = request().body().asJson();
        final RecipeInputJson recipeInput = Json.fromJson(body, RecipeInputJson.class);
        /*try {
            final Recipe recipe = RecipeService.save(recipeInput);
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(Json.toJson(e.getRequestError()));
        }*/
        return ok();
    }
}
