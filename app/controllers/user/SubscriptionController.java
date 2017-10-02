package controllers.user;

import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.notification.NotificationType;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.mvc.Result;
import services.user.UserService;
import util.NotificationManager;

import java.util.Optional;

public class SubscriptionController extends BaseController{

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result subscribe(Long id) {
        User me = getRequester();
        NotificationManager.getInstance().emitToUser(me, id, NotificationType.SUBSCRIPTION, "");
        Optional<User> following = UserService.getInstance().get(id);
        /* Add user to followers. Add follower to user following. */
        following.ifPresent(f -> me.getFollowedUsers().add(f));
        me.update();
        return ok();
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getSubscriptors() {
        User me = getRequester();
        return ok();
    }

}
