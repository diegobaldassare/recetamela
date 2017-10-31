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
import server.RecetamelaUser;
import server.exception.BadRequestException;
import services.MediaService;
import services.NewsService;
import services.recipe.RecipeService;
import services.user.AdminUserService;
import services.user.FollowerService;

import java.util.*;

public class NewsController extends BaseController {

    @Authenticate({ChefUser.class, AdminUser.class})
    public Result create() {
        final News n = getBody(News.class);
        if (getRequester().getType().equals("AdminUser"))
            n.setAuthor(RecetamelaUser.getUser());
        else n.setAuthor(getRequester());

        try {
            NewsService.create(n);
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
        return ok(Json.toJson(n));
    }

    @Authenticate({ChefUser.class, AdminUser.class})
    public Result delete(long id) {
        return NewsService.getInstance().get(id).map(n -> {
            if (!getRequester().getId().equals(n.getAuthor().getId()))
                return unauthorized();
            if (n.getImage() != null) MediaService.getInstance().deleteFile(n.getImage());
            NewsService.getInstance().getFinder().deleteById(id);
            return ok();
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result get(long id) {
        return NewsService.getInstance().get(id).map(n -> ok(Json.toJson(n))).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getUserNews(long id) {
        return ok(Json.toJson(NewsService.getInstance().getUserNews(id)));
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getUserNewsFeed(long id) {
        final SortedSet<News> result = new TreeSet<>(Comparator.comparing(News::getCreated)).descendingSet();
        final NewsService newsService = NewsService.getInstance();
        if (id == -1) loadResult(result, newsService, new Date(System.currentTimeMillis()));
        newsService.get(id).ifPresent(news -> loadResult(result, newsService, news.getCreated()));
//        final List<News> resultList = new ArrayList<>(result);
//        int toIndex = (resultList.size() > 5) ? 5 : resultList.size();
        return ok(Json.toJson(result));
    }

    /**
     * Loads the result set of news with the next 5 older news to the given date
     * that come from the following sources:
     * - News of the users you are following (both Chef and Premium)
     * - News containing a recipe with at least one of the categories you are following
     * - News of the current user
     * - Broadcast news from all Admin users
     * @param result set of news
     * @param date from which the news in the query are loaded
     */
    private void loadResult(SortedSet<News> result, NewsService newsService, Date date) {
        final List<Long> authorsId = new ArrayList<>();
        final List<Long> recipesId = new ArrayList<>();
        authorsId.add(getRequester().getId());
        for (AdminUser adminUser: AdminUserService.getInstance().getAdmins()) {
            authorsId.add(adminUser.getId());
        }
        for (Followers follower: FollowerService.getInstance().getFollowing(getRequester().getId())) {
            authorsId.add(follower.getId());
        }
        for (RecipeCategory category: getRequester().getFollowedCategories()) {
            for (Recipe recipe : RecipeService.getInstance().getRecipesForCategory(category)) {
                recipesId.add(recipe.getId());
            }
        }
        result.addAll(newsService.getTopNewsFeed(authorsId, recipesId, date));
    }

//    private void loadResult(SortedSet<News> result, NewsService newsService, Date date) {
//        result.addAll(newsService.getTopNewsPublishedByUser(getRequester().getId(), date));
//        for (Followers follower : FollowerService.getInstance().getFollowing(getRequester().getId())) {
//            result.addAll(newsService.getTopNewsPublishedByUser(follower.getFollowing().getId(), date));
//        }
//        for (RecipeCategory category : getRequester().getFollowedCategories()) {
//            for (Recipe recipe : RecipeService.getInstance().getRecipesForCategory(category)) {
//                result.addAll(newsService.getTopNewsForRecipe(recipe.getId(), date));
//            }
//        }
//        for (AdminUser a : AdminUserService.getInstance().getAdmins()) {
//            result.addAll(newsService.getTopNewsPublishedByUser(a.getId(), date));
//        }
//    }
}
