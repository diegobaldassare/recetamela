package controllers;

import controllers.authentication.Authenticate;
import models.Followers;
import models.News;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.user.*;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import server.exception.BadRequestException;
import services.MediaService;
import services.NewsService;
import services.recipe.RecipeService;
import services.user.FollowerService;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class NewsController extends BaseController {

    @Authenticate({ChefUser.class})
    public Result create() {
        final News n = getBody(News.class);
        n.setAuthor(getRequester());
        try {
            NewsService.create(n);
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
        return ok(Json.toJson(n));
    }

    @Authenticate({ChefUser.class})
    public Result delete(long id) {
        return NewsService.getInstance().get(id).map(n -> {
            if (!getRequester().getId().equals(n.getAuthor().getId()))
                return unauthorized();
            if (n.getImage() != null) MediaService.getInstance().deleteFile(n.getImage());
            NewsService.getInstance().getFinder().deleteById(id);
            return ok();
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class})
    public Result get(long id) {
        return NewsService.getInstance().get(id).map(n -> ok(Json.toJson(n))).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getUserNewsFeed() {
        final SortedSet<News> result = new TreeSet<>(Comparator.comparing(News::getCreated)).descendingSet();
        final NewsService newsService = NewsService.getInstance();
        result.addAll(newsService.getNewsPublishedByUser(getRequester().getId()));
        for (Followers follower: FollowerService.getInstance().getFollowing(getRequester().getId())) {
            result.addAll(newsService.getNewsPublishedByUser(follower.getFollowing().getId()));
        }
        for (RecipeCategory category: getRequester().getFollowedCategories()) {
            for (Recipe recipe : RecipeService.getInstance().getRecipesForCategory(category)) {
                result.addAll(newsService.getNewsForRecipe(recipe.getId()));
            }
        }
        return ok(Json.toJson(result));
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getUserNews(long id) {
        return ok(Json.toJson(NewsService.getInstance().getUserNews(id)));
    }
}
