package services;

import com.avaje.ebean.Model;
import models.notification.Notification;
import server.ServerMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Matias Cicilia on 23-Sep-17.
 */
public class NotificationService extends Service<Notification> {

    private static NotificationService instance;

    public NotificationService(Model.Finder<Long, Notification> finder){
        super(finder);
    }

    public NotificationService() {
        super(new Model.Finder<>(Notification.class));
    }

    public static NotificationService getInstance() {
        if (instance == null) instance = new NotificationService(new Model.Finder<>(Notification.class));
        return instance;
    }

    public List<Notification> findByUser(Long id) {
        return getFinder().where().eq("receiver", id).findList();
    }

    public List<ServerMessage> getUndeliveredByUser(Long id) {
        List<Notification> undelivered = getFinder().where()
                .eq("receiver", id)
                .eq("delivered", false)
                .findList();
        return undelivered.stream().map(e -> new ServerMessage<>("notification", e)).collect(Collectors.toList());
    }

    public Notification getNullable(Long id) {
        return getFinder().byId(id);
    }
}
