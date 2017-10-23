package controllers;

import controllers.authentication.Authenticate;
import models.chefrequest.ChefRequest;
import models.notification.NotificationType;
import models.user.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.ChefRequestService;
import util.NotificationManager;

import javax.inject.Inject;
import java.util.Optional;

public class ChefRequestController extends BaseController {

    private static Form<ChefRequest> chefRequestForm;

    @Inject
    public ChefRequestController(FormFactory formFactory) {
        chefRequestForm = formFactory.form(ChefRequest.class);
    }

    @Authenticate(PremiumUser.class)
    public Result create() {
        ChefRequest chefRequest = chefRequestForm.bindFromRequest().get();
        chefRequest.setUser(getRequester());
        chefRequest.setAnswered(false);
        chefRequest.save();
        return ok(Json.toJson(chefRequest));
    }

    @Authenticate(AdminUser.class)
    public Result update(Long id) {
        ChefRequest newChefRequest = getBody(ChefRequest.class);
        return ChefRequestService.getInstance().get(id).map(chefRequest -> {
            chefRequest.setAnswered(newChefRequest.isAnswered());
            chefRequest.setAccepted(newChefRequest.isAccepted());

            /* Notify the user his request has been accepted */
            if (newChefRequest.isAccepted()) sendRequestAcceptedNotification(id, getRequester());

            chefRequest.setUser(newChefRequest.getUser());
            chefRequest.setMedia(newChefRequest.getMedia());
            chefRequest.setResume(newChefRequest.getResume());
            chefRequest.update();
            return ok(Json.toJson(chefRequest));
        }).orElse(notFound());
    }

    private void sendRequestAcceptedNotification(Long id, User requester) {
        NotificationManager.getInstance().emitToUser(requester,
                id, NotificationType.REQUEST,
                "Tu solicitud para ser chef ha sido aprobada. Felicidades!",
                id.toString());
    }

    @Authenticate(AdminUser.class)
    public Result get(Long id) {
        return ChefRequestService.getInstance().get(id).map(req -> ok(Json.toJson(req))).orElse(notFound());
    }

    //Checks if the chef request was sent
    @Authenticate({PremiumUser.class, AdminUser.class})
    public Result isUserChefRequest() {
        return Optional.ofNullable(
                ChefRequestService.getInstance().getFinder().query()
                        .where()
                        .eq("user", getRequester())
                        .findUnique())
                .map(chefRequest -> ok(Json.parse("{\"value\" : " + true + "}")))
                .orElse(ok(Json.parse("{\"value\" : " + false + "}")));
    }

    //Checks if the chef request was accepted
    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result isUserChefRequestAccepted() {
        return Optional.ofNullable(
                ChefRequestService.getInstance().getFinder().query()
                        .where()
                        .eq("user", getRequester())
                        .findUnique())
                .map(chefRequest -> ok(Json.parse("{\"value\" : " + chefRequest.isAccepted() + "}")))
                .orElse(ok(Json.parse("{\"value\" : " + false + "}")));
    }

    @Authenticate({AdminUser.class})
    public Result getAll() {
        return ok(Json.toJson(ChefRequestService.getInstance().getFinder().all()));
    }

    @Authenticate({AdminUser.class})
    public Result getAllUnanswered() {
        return ok(Json.toJson(ChefRequestService.getInstance().getFinder().query()
                .where()
                .eq("answered", false)
                .findList()));
    }

    @Authenticate({AdminUser.class})
    public Result delete(Long id) {
        return ChefRequestService.getInstance().get(id).map(chefRequest -> {
            chefRequest.delete();
            return ok();
        }).orElse(notFound());
    }
}