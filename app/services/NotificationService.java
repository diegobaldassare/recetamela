package services;

import com.avaje.ebean.Model;
import models.AuthToken;
import models.Notification;
import models.recipe.Recipe;
import services.recipe.RecipeService;

import java.util.List;
import java.util.Optional;

/**
 * Created by Matias Cicilia on 23-Sep-17.
 */
public class NotificationService extends Service<Notification> {

    private static NotificationService instance;

    private NotificationService(Model.Finder<Long, Notification> finder){
        super(finder);
    }

    public static NotificationService getInstance() {
        if (instance == null) instance = new NotificationService(new Model.Finder<>(Notification.class));
        return instance;
    }

    public List<Notification> findByUser(Long id) {
        return getFinder().where().eq("userId", id).findList();
    }
}
