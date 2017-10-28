package util;

import controllers.ScalaNotificationService;
import models.News;
import models.recipe.RecipeCategory;
import models.user.User;
import server.ServerMessage;
import services.recipe.RecipeCategoryService;
import services.user.FollowerService;

/**
 * Created by Matias Cicilia on 22-Oct-17.
 */
public class NewsManager {
    private static NewsManager instance;

    private NewsManager() {
    }

    public static NewsManager getInstance() {
        if (instance == null) {
            instance = new NewsManager();
        }
        return instance;
    }

    public void notifyReaders(News news, User poster) {
        FollowerService.getInstance().getFollowers(poster.getId()).forEach(
                follower -> {
                    emitToUser(follower.getFollower().getId(), news);
                }
        );
    }

    public void notifyCategoryFollowers(News news) {
        for (RecipeCategory category: news.getRecipe().getCategories()) {
            RecipeCategoryService.getInstance().getFollowers(category.getId()).forEach(follower -> {
                emitToUser(follower.getId(), news);
            });
        }
    }

    private void emitToUser(Long receiverId, News news)  {
        ServerMessage<News> newsServerMessage = new ServerMessage<>("news", news);
        ScalaNotificationService.sendNews(receiverId, newsServerMessage);
    }
}
