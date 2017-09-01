package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.recipe.Recipe;
import models.recipe.json.RecipeInputJson;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.recipe.RecipeService;

public class RecipeController extends Controller {

    public Result create() {
        final JsonNode body = request().body().asJson();
        final RecipeInputJson input = Json.fromJson(body, RecipeInputJson.class);
        if (badCreateRequest(input))
            return badRequest(RequestError.BAD_FORMAT.toString()).as(Http.MimeTypes.JSON);
        try {
            final Recipe recipe = RecipeService.getInstance().save(input);
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    private boolean badCreateRequest(RecipeInputJson input) {
        return
                input.name == null ||
                input.name.length() == 0 ||
                input.description == null ||
                input.description.length() == 0 ||
                input.steps == null ||
                input.steps.length() == 0 ||
                (input.videoUrl != null && input.videoUrl.length() == 0) ||
                (input.difficulty < 1 || input.difficulty > 5) ||
                input.categoryIds == null ||
                input.categoryIds.length == 0 ||
                input.ingredientNames == null ||
                input.ingredientNames.length == 0;
    }

    public Result get(long id) {
        final Recipe recipe = RecipeService.getInstance().get(id);
        if (recipe == null) return notFound();
        return ok(Json.toJson(recipe));
    }
}
