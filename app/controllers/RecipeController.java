package controllers;

import models.recipe.Recipe;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.exception.BadRequestException;
import services.recipe.RecipeFormatter;
import services.recipe.RecipeService;
import services.recipe.RecipeValidator;

import java.util.Optional;

class RecipeController extends BaseController {

    // @Authenticate(PremiumUser.class)
    public Result create(Recipe r) {
        r.setAuthor(getRequester());
        RecipeFormatter.format(r);
        try {
            RecipeValidator.validateAllFields(r);
            r.save();
            return ok(Json.toJson(r));
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
    public Result modify(long id, Recipe r) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        if (!recipe.isPresent()) return notFound();
        // if (!recipe.get().getAuthor().equals(getRequester())) return unauthorized();
        RecipeFormatter.format(r);
        try {
            RecipeValidator.validateNotNullFields(r);
            RecipeService.getInstance().modify(recipe.get(), r);
            r.save();
            return ok(Json.toJson(recipe.get()));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }
}
