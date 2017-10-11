package controllers.recipe;

import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.notification.NotificationType;
import models.recipe.Recipe;
import models.recipe.RecipeRating;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import server.exception.BadRequestException;
import services.recipe.RecipeRatingService;
import services.recipe.RecipeService;
import util.NotificationManager;

import java.util.Optional;

public class RecipeRatingController extends BaseController {

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result create(long recipeId) {
        final RecipeRating recipeRating = getBody(RecipeRating.class);
        recipeRating.setUser(getRequester());

        final Optional<Recipe> recipe = RecipeService.getInstance().get(recipeId);
        if (!recipe.isPresent()) return notFound();
        if (recipe.get().getAuthor().getId().equals(getRequester().getId())) {
            return unauthorized();
        }
        final Recipe r = recipe.get();
        try {
            RecipeRatingService.getInstance().addRating(r, recipeRating);
            NotificationManager.getInstance().emitToUser(getRequester(),
                    r.getAuthor().getId(),
                    NotificationType.RATING,
                    " acaba de hacer un rating de " + recipeRating.getRating() + " a tu receta " + r.getName(), r.getId().toString());
            return ok(Json.toJson(r));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getRatingFromUser(long recipeId) {
        final User user = getRequester();
        return RecipeService.getInstance().get(recipeId).map(recipe -> {
            RecipeRating recipeRating = RecipeRatingService.getInstance().getRatingByUser(user.getId(), recipe);
            return ok(Json.toJson(recipeRating));
        }).orElse(notFound());

    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result updateRating(long recipeRatingId) {
        final Optional<RecipeRating> oldRecipeRating = RecipeRatingService.getInstance().get(recipeRatingId);
        if(!oldRecipeRating.isPresent()) return notFound();
        final RecipeRating r = oldRecipeRating.get();
        final RecipeRating recipeRating = getBody(RecipeRating.class); //New recipeRating
        try {
            RecipeRatingService.getInstance().updateRating(recipeRating, r);
            return ok(Json.toJson(r));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }

    }
}
