package controllers;
import controllers.authentication.Authenticate;
import models.user.*;
import play.mvc.*;
import play.libs.*;

import util.NotificationManager;

/**
 * Created by Matias Cicilia on 22-Sep-17.
 */
public class NotificationController extends BaseController {

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getNotifications() {
        User sender = getRequester();
        NotificationManager.EventOutput eventOutput = NotificationManager.getInstance().requestNotifications(sender.getId());
        if (eventOutput == null || !eventOutput.isUpdated()) {
            return ok();
        }
        return ok().chunked(eventOutput.getSource().via(EventSource.flow())).as(Http.MimeTypes.EVENT_STREAM);
    }

}


