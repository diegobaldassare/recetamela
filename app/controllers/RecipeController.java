package controllers;

import models.recipe.Recipe;
import models.recipe.RecipeInput;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.exception.BadRequestException;
import services.recipe.RecipeFormatService;
import services.recipe.RecipeService;

import java.util.Optional;

public class RecipeController extends Controller {

    // @Authenticate(PremiumUser.class)
    public Result create() {
        final RecipeInput input = Json.fromJson(request().body().asJson(), RecipeInput.class);
        try {
            RecipeFormatService.formatInput(input);
            final Recipe recipe = RecipeService.getInstance().save(input, SecurityController.getUser());
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    // @Authenticate({FreeUser.class, PremiumUser.class})
    public Result get(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }

    // @Authenticate(PremiumUser.class)
    public Result modify(long id) {
        final Optional<Recipe> recipeOpt = RecipeService.getInstance().get(id);
        if (!recipeOpt.isPresent()) return notFound();
        final Recipe recipe = recipeOpt.get();
        // if (!recipe.getAuthor().equals(SecurityController.getUser())) return unauthorized();
        final RecipeInput input = Json.fromJson(request().body().asJson(), RecipeInput.class);
        try {
            if (input.name != null) recipe.setName(RecipeFormatService.formatName(input.name));
            if (input.description != null) recipe.setDescription(RecipeFormatService.formatDescription(input.description));
            if (input.difficulty != 0) recipe.setDifficulty(RecipeFormatService.formatDifficulty(input.difficulty));
            recipe.setVideoUrl(RecipeFormatService.formatVideoUrl(input.videoUrl));
            if (input.steps != null) RecipeService.getInstance().setSteps(recipe, RecipeFormatService.formatSteps(input.steps));
            if (input.categoryNames != null) {
                recipe.getCategories().clear();
                RecipeService.getInstance().setCategories(recipe, RecipeFormatService.formatCategoryOrIngredientNames(input.categoryNames));
            }
            if (input.ingredientNames != null) {
                recipe.getIngredients().clear();
                RecipeService.getInstance().setIngredients(recipe, RecipeFormatService.formatCategoryOrIngredientNames(input.ingredientNames));
            }
            if (input.imageIds != null) {
                recipe.getImages().clear();
                RecipeService.getInstance().setImages(recipe, RecipeFormatService.formatImageIds(input.imageIds));
            }
            recipe.update();
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    public Result delete(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        return recipe.map(r -> {
            r.delete();
            return ok();
        }).orElseGet(Results::notFound);
    }
}
