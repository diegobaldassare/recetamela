package controllers;

import models.chefrequest.ChefRequest;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.ChefRequestService;

import javax.inject.Inject;
import java.util.Optional;

public class ChefRequestController extends BaseController {

    private static Form<ChefRequest> chefRequestForm;

    @Inject
    public ChefRequestController(FormFactory formFactory) {
        chefRequestForm = formFactory.form(ChefRequest.class);
    }

    public Result create() {
        ChefRequest chefRequest = chefRequestForm.bindFromRequest().get();
        chefRequest.setUser(getRequester());
        chefRequest.setAnswered(false);
        chefRequest.save();
        return ok(Json.toJson(chefRequest));
    }

    public Result update(Long id) {
        ChefRequest newChefRequest = chefRequestForm.bindFromRequest().get();
        return ChefRequestService.getInstance().get(id).map(chefRequest -> {
            chefRequest.setAnswered(newChefRequest.isAnswered());
            chefRequest.setAccepted(newChefRequest.isAccepted());
            chefRequest.setUser(newChefRequest.getUser());
            chefRequest.setMedia(newChefRequest.getMedia());
            chefRequest.setResume(newChefRequest.getResume());
            chefRequest.update();
            return ok(Json.toJson(chefRequest));
        }).orElse(notFound());
    }

    public Result get(Long id) {
        return ChefRequestService.getInstance().get(id).map(req -> ok(Json.toJson(req))).orElse(notFound());
    }

    //Checks if the chef request was sent
    public Result isUserChefRequest() {
        return Optional.ofNullable(
                ChefRequestService.getInstance().getFinder().query()
                        .where()
                        .eq("user", getRequester())
                        .findUnique())
                .map(chefRequest -> ok(Json.toJson(true)))
                .orElse(ok(Json.toJson(false)));
    }

    //Checks if the chef request was accepted
    public Result isUserChefRequestAccepted() {
        return Optional.ofNullable(
                ChefRequestService.getInstance().getFinder().query()
                        .where()
                        .eq("user", getRequester())
                        .findUnique())
                .map(chefRequest -> ok(Json.toJson(chefRequest.isAccepted())))
                .orElse(ok(Json.toJson(false)));
    }

    public Result getAll() {
        return ok(Json.toJson(ChefRequestService.getInstance().getFinder().all()));
    }

    public Result delete(Long id) {
        return ChefRequestService.getInstance().get(id).map(chefRequest -> {
            chefRequest.delete();
            return ok();
        }).orElse(notFound());
    }
}
