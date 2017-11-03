package controllers.recipe;

import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.notification.NotificationType;
import models.recipe.Recipe;
import models.recipe.RecipeRating;
import models.user.*;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import server.exception.BadRequestException;
import services.recipe.RecipeRatingService;
import services.recipe.RecipeService;
import util.NotificationManager;

import java.util.List;
import java.util.Optional;

public class RecipeRatingController extends BaseController {

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
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

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getRatingFromUser(long recipeId) {
        final User user = getRequester();
        return RecipeService.getInstance().get(recipeId).map(recipe -> {
            RecipeRating recipeRating = RecipeRatingService.getInstance().getRatingByUser(user.getId(), recipe);
            return ok(Json.toJson(recipeRating));
        }).orElse(notFound());

    }

    @Authenticate({ChefUser.class})
    public Result likeByChef(long recipeId) {
        final User user = getRequester();
        final Optional<Recipe> recipe = RecipeService.getInstance().get(recipeId);
        if (!recipe.isPresent()) return notFound();
        if (recipe.get().getAuthor().getId().equals(getRequester().getId())) {
            return unauthorized();
        }
        final Recipe r = recipe.get();
        r.getLikesByChef().add(user);
        r.save();
        return ok(Json.toJson(r));
    }

    @Authenticate({ChefUser.class})
    public Result dislikeByChef(long recipeId) {
        final User user = getRequester();
        final Optional<Recipe> recipe = RecipeService.getInstance().get(recipeId);
        if (!recipe.isPresent()) return notFound();
        if (recipe.get().getAuthor().getId().equals(getRequester().getId())) {
            return unauthorized();
        }
        final Recipe r = recipe.get();
        r.getLikesByChef().remove(user);
        r.save();
        return ok(Json.toJson(r));
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getLikesRecipeByChef(long recipeId) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(recipeId);
        if (!recipe.isPresent()) return notFound();
        final Recipe r = recipe.get();
        List<User> chefLikes = r.getLikesByChef();
        return ok(Json.toJson(chefLikes));
    }

//    @Authenticate({FreeUser.class, PremiumUser.class})
//    public Result updateRating(long recipeId, long recipeRatingId) {
//        final Optional<RecipeRating> oldRecipeRating = RecipeRatingService.getInstance().get(recipeRatingId);
//        if(!oldRecipeRating.isPresent()) return notFound();
//        final RecipeRating r = oldRecipeRating.get();
//        final RecipeRating recipeRating = getBody(RecipeRating.class); //New recipeRating
//        ///
//        final Optional<Recipe> recipe = RecipeService.getInstance().get(recipeId);
//        if (!recipe.isPresent()) return notFound();
//        if (recipe.get().getAuthor().getId().equals(getRequester().getId())) {
//            return unauthorized();
//        }
//        final Recipe recipe1 = recipe.get();
//        ////
//        try {
//            RecipeRatingService.getInstance().updateRating(recipe1, recipeRating, r);
//            return ok(Json.toJson(r));
//        } catch (BadRequestException e) {
//            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
//        }
//    }
}
