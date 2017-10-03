package controllers.user;

import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.Followers;
import models.notification.NotificationType;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.libs.Json;
import play.mvc.Result;
import services.user.FollowerService;
import services.user.UserService;
import util.NotificationManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubscriptionController extends BaseController{

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result subscribe(Long id) {
        User me = getRequester();
        Optional<User> following = UserService.getInstance().get(id);

        Followers followers = new Followers();
        followers.setFollower(me);
        following.ifPresent(followers::setFollowing);
        followers.save();

        //Optional<Followers> followers = FollowerService.getInstance().getByUser(id);
        NotificationManager.getInstance().emitToUser(me, id, NotificationType.SUBSCRIPTION, "");

        return ok();
    }


    public Result getFollowers(Long id) {
        List<Followers> followers = FollowerService.getInstance().getFollowers(id);
        List<User> mappedFollowers = followers.stream().map(Followers::getFollower).collect(Collectors.toList());
        return ok(Json.toJson(mappedFollowers));
    }

    public Result getFollowing(Long id) {
        List<Followers> followers = FollowerService.getInstance().getFollowing(id);
        List<User> mappedFollowing = followers.stream().map(Followers::getFollowing).collect(Collectors.toList());
        return ok(Json.toJson(mappedFollowing));
    }

}
