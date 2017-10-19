package controllers.user;

import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.Followers;
import models.notification.NotificationType;
import models.user.*;
import play.libs.Json;
import play.mvc.Result;
import services.user.FollowerService;
import services.user.UserService;
import util.NotificationManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubscriptionController extends BaseController{

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result subscribe(Long id) {
        User me = getRequester();
        Optional<User> following = UserService.getInstance().get(id);

        if (!following.isPresent()) return notFound();
        Followers followers = new Followers();
        followers.setFollower(me);
        following.ifPresent(followers::setFollowing);
        followers.save();

        NotificationManager.getInstance().emitToUser(me, id, NotificationType.SUBSCRIPTION, "se subscribó a tu perfil", me.getId().toString());

        return ok(Json.toJson(me));
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result unSubscribe(Long id) {
        User me = getRequester();

        Optional<Followers> followers = FollowerService.getInstance().getByIds(me.getId(), id);
        followers.ifPresent(Followers::delete);

        return ok(Json.toJson(me));
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
