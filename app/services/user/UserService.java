package services.user;

import com.avaje.ebean.Model;
import models.user.User;
import services.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService extends Service<User> {
    private static UserService instance;

    private UserService(Model.Finder<Long, User> finder) {
        super(finder);
    }

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService(new Model.Finder<>(User.class));
        return instance;
    }

    public Optional<User> findByFacebookId(Long id) {
        return Optional.ofNullable(getFinder().where().eq("facebook_id", id).findUnique());
    }

    public Optional<User> findByAuthToken(String authToken) {
        return Optional.ofNullable(getFinder().where().eq("auth_token", authToken).findUnique());
    }

    public void modify(User u, User input) {
        if (input.getName() != null) u.setName(input.getName());
        if (input.getLastName() != null) u.setLastName(input.getLastName());
        if (input.getEmail() != null) u.setEmail(input.getEmail());
    }

    public List<User> getAll() {
        return getFinder().all();
    }

    public List<User> findAllByName(String name) {
        final String[] nameParts = name.toLowerCase().trim().split(" ");
        return getAll().stream().filter(u -> {
            final String n = (u.getName() + u.getLastName()).toLowerCase().replaceAll("[^a-z ]", "");
            for (final String namePart : nameParts) if (n.contains(namePart)) return true;
            return false;
        }).collect(Collectors.toList());
    }
}
