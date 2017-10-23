package util;

import controllers.ScalaNotificationService;
import models.News;
import models.user.User;
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
        System.out.println(FollowerService.getInstance().getFollowers(poster.getId()));
        FollowerService.getInstance().getFollowers(poster.getId()).forEach(
                follower -> {
                    emitToUser(follower.getFollower().getId(), news);
                    System.out.println("notifying: " + follower.getFollower().getId());
                }
        );
    }

    private void emitToUser(Long receiverId, News news)  {
        ScalaNotificationService.sendNews(receiverId, news);
    }
}
