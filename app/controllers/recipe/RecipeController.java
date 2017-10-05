package controllers.recipe;

import com.avaje.ebean.Ebean;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.Media;
import models.recipe.RecipeSearchQuery;
import models.recipe.RecipeStep;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.recipe.Recipe;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.exception.BadRequestException;
import services.MediaService;
import services.recipe.RecipeBookService;
import services.recipe.RecipeFormatter;
import services.recipe.RecipeService;
import services.recipe.RecipeValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeController extends BaseController {

    @Authenticate(PremiumUser.class)
    public Result create() {
        final Recipe r = getBody(Recipe.class);
        r.setAuthor((PremiumUser) getRequester());
        RecipeFormatter.format(r);
        try {
            RecipeValidator.validateAllFields(r);
            r.save();
            return ok(Json.toJson(r));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result get(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }

    @Authenticate(PremiumUser.class)
    public Result modify(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        if (!recipe.isPresent()) return notFound();
        if (!recipe.get().getAuthor().getId().equals(getRequester().getId()))
            return unauthorized();
        final Recipe r = getBody(Recipe.class);
        RecipeFormatter.format(r);
        try {
            RecipeValidator.validateNotNullFields(r);
            RecipeService.getInstance().modify(recipe.get(), r);
            recipe.get().save();
            return ok(Json.toJson(recipe.get()));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    @Authenticate(PremiumUser.class)
    public Result delete(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        final MediaService mediaService = MediaService.getInstance();
        return recipe.map(r -> {
            final List<Media> images = new ArrayList<>(r.getImages());
            r.getSteps().stream()
                    .map(RecipeStep::getImage)
                    .filter(Objects::nonNull)
                    .forEach(i -> mediaService.deleteFile(mediaService.getFile(i.getName())));

            RecipeBookService.getInstance().getFinder().query()
                    .where()
                    .in("recipes", r)
                    .findList()
                    .forEach(recipeBook -> {
                        recipeBook.getRecipes().remove(r);
                        recipeBook.update();
                    });

            r.delete();
            images.forEach(mediaService::delete);
            return ok();
        }).orElseGet(Results::notFound);
    }

    @Authenticate({ FreeUser.class, PremiumUser.class })
    public Result search(String name, String categories, String ingredients, String difficulty, String author) {
        final RecipeSearchQuery q = new RecipeSearchQuery(
                name.toLowerCase().trim(),
                categories.toLowerCase().trim(),
                ingredients.toLowerCase().trim(),
                difficulty.trim(),
                author.toLowerCase().trim());
        if (getRequester().getType().equals("FreeUser") && !q.ingredients.isEmpty()) q.ingredients.clear();
        final List<Recipe> results = RecipeService.getInstance().search(q);
        return ok(Json.toJson(results));
    }
}
