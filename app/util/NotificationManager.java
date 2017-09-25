package util;

import akka.stream.javadsl.Source;
import models.Notification;
import play.Logger;
import play.libs.EventSource;
import play.mvc.Http;
import play.mvc.Result;
import services.NotificationService;
import services.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

/**
 * Created by Matias Cicilia on 22-Sep-17.
 */
public class NotificationManager {
    private static NotificationManager instance;
    private Map<Long, Source<EventSource.Event, ?>> userEventsMap;

    private NotificationManager() {
        this.userEventsMap = new HashMap<>();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public Result subscribe(long userId) {
        Source<EventSource.Event, ?> eventSource = userEventsMap.get(userId);
        if (eventSource == null) {
            /* Levanto de la DB todas las notificaciones que se perdio */
            eventSource = fetchMissedEvents(userId);
        }
        NotificationService.getInstance().findByUser(userId).forEach(System.out::println);
        return ok().chunked(eventSource.via(EventSource.flow())).as(Http.MimeTypes.EVENT_STREAM);
    }

    public void emitToUser(long userId, String name, String data)  {
        Notification notification = new Notification(userId, name, data);
        Source<EventSource.Event, ?> eventOutput = userEventsMap.get(userId);
        if (eventOutput == null) {
            eventOutput = Source.single(notification.toEvent());
            userEventsMap.put(userId, eventOutput);
        }
        notification.save();
    }

    private Source<EventSource.Event, ?> fetchMissedEvents(long userId) {
        Source<EventSource.Event, ?> eventSource;
        List<Notification> missedEvents = NotificationService.getInstance()
                .findByUser(userId)
                .stream()
                .filter(n -> !n.isDelivered())
                .collect(Collectors.toList());

        /* Armo mi stream de eventos con las notificaciones */
        eventSource = Source.from(missedEvents.stream().map(Notification::toEvent).collect(Collectors.toList()));
        userEventsMap.put(userId, eventSource);
        return eventSource;
    }


}
