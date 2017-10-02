package util;

import akka.stream.javadsl.Source;
import models.notification.Notification;
import models.notification.NotificationType;
import models.user.User;
import play.Logger;
import play.libs.EventSource;
import services.NotificationService;
import sun.rmi.runtime.Log;

import java.util.*;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

/**
 * Created by Matias Cicilia on 22-Sep-17.
 */
public class NotificationManager {
    private static NotificationManager instance;
    private Map<Long, Queue<EventOutput>> events;

    private NotificationManager() {
        events = new HashMap<>();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void subscribe(long userId) {
        /* Levanto de la DB todas las notificaciones que se perdio */
        EventOutput output = fetchMissedEvents(userId);
        Queue<EventOutput> eventsQueue = new LinkedList<>();
        if (output.updated) eventsQueue.add(output);
        events.put(userId, eventsQueue);
    }

    public EventOutput requestNotifications(long userId) {
        Queue<EventOutput> queue = events.get(userId);
        if (queue != null && !queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    public void unsubscribe(long userId) {
        events.remove(userId);
    }

    public void emitToUser(User sender, Long receiverId, NotificationType name, String message)  {
        Notification notification = new Notification(sender.getId(), sender.getName() + " " + sender.getLastName(), receiverId, sender.getProfilePic(), name, message);
        Queue<EventOutput> queue = events.get(receiverId);
        if (queue != null) {
            EventOutput event = new EventOutput();
            event.source = Source.single(notification.toEvent());
            event.updated = true;
            queue.add(event);;
            notification.setDelivered(true);
        }
        notification.save();
    }

    private EventOutput fetchMissedEvents(long userId) {
        List<Notification> missedEvents = NotificationService.getInstance().getUndeliveredByUser(userId);

        /* Armo mi stream de eventos con las notificaciones */
        EventOutput eventOutput = new EventOutput();
        eventOutput.updated = !missedEvents.isEmpty();
        eventOutput.source = Source.from(missedEvents.stream().map(Notification::toEvent).collect(Collectors.toList()));
        missedEvents.forEach(e -> {
            e.setDelivered(true);
            e.update();
        });
        return eventOutput;
    }

    public class EventOutput {
        private Source<EventSource.Event, ?> source;
        private boolean updated;

        public Source<EventSource.Event, ?> getSource() {
            return source;
        }

        public void setSource(Source<EventSource.Event, ?> source) {
            this.source = source;
        }

        public boolean isUpdated() {
            return updated;
        }

        public void setUpdated(boolean updated) {
            this.updated = updated;
        }
    }


}
