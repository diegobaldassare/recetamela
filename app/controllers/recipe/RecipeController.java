package controllers.recipe;

import com.avaje.ebean.*;
import com.google.common.collect.Lists;
import controllers.BaseController;
import controllers.NewsController;
import controllers.authentication.Authenticate;
import models.Comment;
import models.Media;
import models.News;
import models.notification.NotificationType;
import models.recipe.RecipeSearchQuery;
import models.recipe.RecipeStep;
import models.user.AdminUser;
import models.user.ChefUser;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.recipe.Recipe;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.exception.BadRequestException;
import services.MediaService;
import services.NewsService;
import services.recipe.RecipeBookService;
import services.recipe.RecipeFormatter;
import services.recipe.RecipeService;
import services.recipe.RecipeValidator;
import util.NewsManager;
import util.NotificationManager;

import java.util.*;

public class RecipeController extends BaseController {

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result create() {
        final Recipe r = getBody(Recipe.class);
        r.setAuthor((PremiumUser) getRequester());
        RecipeFormatter.format(r);
        try {
            RecipeValidator.validateAllFields(r);
            r.save();
            NotificationManager.getInstance().notifyFollowers(getRequester(), NotificationType.RECIPE, "subió una nueva receta: " + r.getName(), r.getId().toString());
            r.getCategories().forEach(category -> {
                NotificationManager.getInstance().notifyCategoryFollowers(getRequester(), NotificationType.CATEGORY, "subió una nueva receta: " + "'" + r.getName() + "'" + " a la categoría " + category.getName(), category.getId().toString());
            });

            createNewsForRecipe(r);
            return ok(Json.toJson(r));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    private void createNewsForRecipe(Recipe r) throws BadRequestException {
        News news = new News();
        news.setRecipe(r);
        news.setTitle("Receta");
        news.setDescription(r.getDescription());
        news.setAuthor(r.getAuthor());
        NewsService.create(news);
        NewsManager.getInstance().notifyReaders(news, r.getAuthor());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result get(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
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

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result delete(long id) {
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        final MediaService mediaService = MediaService.getInstance();
        return recipe.map(r -> {
            final List<Media> images = new ArrayList<>(r.getImages());
            r.getSteps().stream()
                    .map(RecipeStep::getImage)
                    .filter(Objects::nonNull)
                    .forEach(mediaService::deleteFile);

            RecipeBookService.getInstance().getFinder().query()
                    .where()
                    .in("recipes", r)
                    .findList()
                    .forEach(recipeBook -> {
                        recipeBook.getRecipes().remove(r);
                        recipeBook.update();
                    });

            NewsService.getInstance().getNewsForRecipe(id).forEach(News::delete);

            r.delete();
            images.forEach(mediaService::delete);
            return ok();
        }).orElseGet(Results::notFound);
    }

    public Result getUserRecipes(long userId){
        List<Recipe> recipes = RecipeService.getInstance().getUserRecipes(userId);
        return ok(Json.toJson(Lists.reverse(recipes)));
    }

    public Result getRecipeAuthor(long id){
        final Optional<Recipe> recipe = RecipeService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r.getAuthor()))).orElseGet(Results::notFound);
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
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

    public Result getRecipeComments(Long recipeId) {
        return ok(Json.toJson(Ebean
                .createQuery(Comment.class, "where recipe_id = :id order by date desc")
                .setParameter("id", recipeId)
                .findList()));
    }
}
