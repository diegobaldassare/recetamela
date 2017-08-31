package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.recipe.Recipe;
import models.recipe.json.RecipeInputJson;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import server.exception.BadRequestException;

public class RecipeController extends Controller {

    public Result create() {
        final JsonNode body = request().body().asJson();
        final RecipeInputJson recipeInput = Json.fromJson(body, RecipeInputJson.class);
        /*try {
            final Recipe recipe = RecipeService.getInstance().save(recipeInput);
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }*/
        return ok();
    }

    public Result get(long id) {
        /*final Recipe recipe = RecipeService.getById(id);
        if (recipe == null) return notFound();
        return ok(Json.toJson(recipe));*/
        return ok();
    }
}
