package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.recipe.Recipe;
import models.recipe.RecipeInput;
import org.apache.commons.lang3.StringUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.recipe.RecipeService;

import java.util.Optional;

public class RecipeController extends Controller {

    public Result create(Long userId) {
        final JsonNode body = request().body().asJson();
        final RecipeInput input = Json.fromJson(body, RecipeInput.class);
        if (badCreateRequest(input))
            return badRequest(RequestError.BAD_FORMAT.toString()).as(Http.MimeTypes.JSON);
        try {
            final Recipe recipe = RecipeService.getInstance().save(input, userId);
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    private boolean badCreateRequest(RecipeInput input) {
        return
                input.name == null ||
                input.name.length() < 2 ||
                !StringUtils.isAlphanumericSpace(input.name) ||
                input.description == null ||
                input.description.length() == 0 ||
                input.steps == null ||
                input.steps.length() == 0 ||
                (input.videoUrl != null && input.videoUrl.length() == 0) ||
                (input.difficulty < 1 || input.difficulty > 5) ||
                input.categoryNames == null ||
                input.categoryNames.length == 0 ||
                input.ingredientNames == null ||
                input.ingredientNames.length == 0;
    }

    public Result get(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }
}
