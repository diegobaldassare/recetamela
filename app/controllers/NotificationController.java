package controllers;
import akka.stream.javadsl.Source;
import controllers.authentication.Authenticate;
import models.Notification;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.Logger;
import play.mvc.*;
import play.libs.*;

import util.NotificationManager;

/**
 * Created by Matias Cicilia on 22-Sep-17.
 */
public class NotificationController extends BaseController {

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getNotifications() {
        User sender = getRequester();

        Logger.debug("Requesting notifications for " + sender.getName());

        return NotificationManager.getInstance().subscribe(sender.getId());
    }

}


