package controllers;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import models.FreeUser;
import models.PremiumUser;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.PremiumUserService;
import services.FreeUserService;

import java.util.List;
import java.util.Optional;

public class PremiumUserController extends Controller {

    private static Form<PremiumUser> userForm;

    @Inject
    public PremiumUserController(FormFactory formFactory) {
        userForm =  formFactory.form(PremiumUser.class);
    }

    public Result createPremiumUser() {
        PremiumUser user = userForm.bindFromRequest().get();
        user.save();
        return ok(Json.toJson(user));
    }

    public Result createPremiumUser(Long freeUserId) {
        FreeUser user;
        Optional<FreeUser> userOptional = FreeUserService.getInstance().get(freeUserId);
        if(userOptional.isPresent()) {
            user = userOptional.get();
            PremiumUser premiumUser = new PremiumUser(user.getName(), user.getLastName(), user.getEmail(), user.getProfilePic());
            Ebean.execute(() -> {
                user.delete();
                premiumUser.save();
            });
            return ok(Json.toJson(premiumUser));
        }
        return notFound("None User associated to ID found");
    }

    public Result updatePremiumUser() {
        //..Falta
        return ok();
    }

    public Result getPremiumUsers(){
        List<PremiumUser> premiumUsers = PremiumUserService.getInstance().getFinder().all();
        return ok(Json.toJson(premiumUsers));
    }

    public Result deletePremiumUser(Long id){
        Optional<PremiumUser> user = PremiumUserService.getInstance().get(id);
        if (user.isPresent()){
            user.get().delete();
            return ok();
        }
        return notFound();
    }

    public Result getPremiumUser(Long id) {
        final Optional<PremiumUser> user = PremiumUserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElseGet(Results::notFound);
    }
}
