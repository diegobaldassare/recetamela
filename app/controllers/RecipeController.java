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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeController extends Controller {

    public Result create() {
        final JsonNode body = request().body().asJson();
        final RecipeInput input = Json.fromJson(body, RecipeInput.class);
        if (badCreateRequest(input))
            return badRequest(RequestError.BAD_FORMAT.toString()).as(Http.MimeTypes.JSON);
        try {
            final Recipe recipe = RecipeService.getInstance().save(input);
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    private boolean badCreateRequest(RecipeInput input) {
        if (input.videoUrl != null) {
            input.videoUrl = input.videoUrl.trim();
            if (input.videoUrl.length() == 0) input.videoUrl = null;
        }

        if (input.name == null) return true;
        else input.name = capitalizeFirstCharacter(input.name).trim();
        if (!alphaNumSpaceNotEmpty(input.name)) return true;
        input.name = capitalizeFirstCharacter(input.name);

        if (input.description == null) return true;
        else input.description = capitalizeFirstCharacter(input.description).trim();
        if (input.description.length() == 0) return true;

        if (input.difficulty < 1 || input.difficulty > 5) return true;

        if (input.steps == null) return true;
        final List<String> steps = Arrays.asList(input.steps);
        for (int i = 0; i < steps.size(); i++) {
            steps.set(i, capitalizeFirstCharacter(steps.get(i)).trim());
            if (steps.get(i).length() == 0) steps.remove(i);
        }
        if (steps.isEmpty()) return true;
        input.steps = (String[]) steps.toArray();

        if (input.categoryNames == null) return true;
        final List<String> categoryNames = Arrays.asList(input.categoryNames);
        for (int i = 0; i < categoryNames.size(); i++) {
            categoryNames.set(i, categoryNames.get(i).trim().toLowerCase());
            if (!alphaNumSpaceNotEmpty(categoryNames.get(i))) categoryNames.remove(i);
        }
        if (categoryNames.isEmpty()) return true;
        input.categoryNames = (String[]) categoryNames.toArray();

        if (input.ingredientNames == null) return true;
        final List<String> ingredientNames = Arrays.asList(input.ingredientNames);
        for (int i = 0; i < ingredientNames.size(); i++) {
            ingredientNames.set(i, ingredientNames.get(i).trim().toLowerCase());
            if (!alphaNumSpaceNotEmpty(ingredientNames.get(i))) ingredientNames.remove(i);
        }
        if (ingredientNames.isEmpty()) return true;
        input.ingredientNames = (String[]) ingredientNames.toArray();

        return false;
    }

    private boolean alphaNumSpaceNotEmpty(String s) {
        return s.length() != 0 && StringUtils.isAlphanumericSpace(s);
    }

    private String capitalizeFirstCharacter(String text) {
        if (text.length() < 2) return "" + Character.toUpperCase(text.charAt(0));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    public Result get(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }
}
