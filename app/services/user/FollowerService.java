package services.user;

import com.avaje.ebean.Model;
import models.Followers;
import services.Service;

import java.util.List;
import java.util.Optional;

public class FollowerService extends Service<Followers> {
    private static FollowerService instance;

    private FollowerService(Model.Finder<Long, Followers> finder) {
        super(finder);
    }

    public static FollowerService getInstance() {
        if (instance == null)
            instance = new FollowerService(new Model.Finder<>(Followers.class));
        return instance;
    }

    public List<Followers> getFollowers(Long id) {
        return getFinder().fetch("follower").where().eq("following_id", id).findList();
    }

    public List<Followers> getFollowing(Long id) {
        return getFinder().fetch("following").where().eq("follower_id", id).findList();
    }

    public Optional<Followers> getByIds(Long followerId, Long followingId) {
        return Optional.ofNullable(getFinder().where()
                .eq("follower_id", followerId)
                .eq("following_id", followingId)
                .findUnique());
    }
}

