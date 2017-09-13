package controllers.user;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import models.FreeUser;
import models.PremiumUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.PremiumUserService;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PremiumUserController extends Controller {

    private static Form<PremiumUser> userForm;

    @Inject
    public PremiumUserController(FormFactory formFactory) {
        userForm =  formFactory.form(PremiumUser.class);
    }

    public Result createPremiumUser() {
        PremiumUser user = userForm.bindFromRequest().get();
        user.setExpirationDate(LocalDate.now().plus(Period.ofMonths(1)));
        user.save();
        return ok(Json.toJson(user));
    }


    public Result upgradePremiumUser() {
        //Para upgradear a Chef
        return ok();
    }

    public Result downgradePremiumUser() {
        try {
            return updatePremiumUser(user -> {
                FreeUser freeUser = new FreeUser(user.getName(), user.getLastName(), user.getEmail(), user.getProfilePic());
                freeUser.setId(user.getId());
                freeUser.setFacebookId(user.getFacebookId());
                freeUser.setAuthToken(user.getAuthToken());
                Ebean.execute(() -> {
                    user.delete();
                    freeUser.save();
                });
                return ok(Json.toJson(freeUser));
            });
        } catch (NoSuchObjectException err) {
            return notFound(err.getMessage());
        }
    }

    public Result updatePremiumUser() {
        try {
            return updatePremiumUser(user -> {
                user.update();
                return ok(Json.toJson(user));
            });
        } catch (NoSuchObjectException err) {
            return notFound(err.getMessage());
        }
    }

    public Result updatePremiumUserExpirationDate() {
        try {
            return updatePremiumUser(user -> {
                ((PremiumUser) user).setExpirationDate(LocalDate.now().plus(Period.ofMonths(1)));
                user.update();
                return ok(Json.toJson(user));
            });
        } catch (NoSuchObjectException err) {
            return notFound(err.getMessage());
        }
    }

    public Result getPremiumUser(Long id) {
        final Optional<PremiumUser> user = PremiumUserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElseGet(Results::notFound);
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

    private Result updatePremiumUser(Function<User, Result> function) throws NoSuchObjectException {
        PremiumUser newPremiumUser = userForm.bindFromRequest().get();
        Optional<PremiumUser> optionalPremiumUser = PremiumUserService.getInstance().get(newPremiumUser.getId());
        PremiumUser oldPremiumUser;
        if(optionalPremiumUser.isPresent()) oldPremiumUser = optionalPremiumUser.get();
        else throw new NoSuchObjectException("The user was not found");
        if (newPremiumUser.getName() != null) oldPremiumUser.setName(newPremiumUser.getName());
        if (newPremiumUser.getLastName() != null) oldPremiumUser.setLastName(newPremiumUser.getLastName());
        if (newPremiumUser.getEmail() != null) oldPremiumUser.setEmail(newPremiumUser.getEmail());
        if (newPremiumUser.getProfilePic() != null) oldPremiumUser.setProfilePic(newPremiumUser.getProfilePic());
        if (newPremiumUser.getAuthToken() != null) oldPremiumUser.setAuthToken(newPremiumUser.getAuthToken());
        if (newPremiumUser.getRecipes() != null) oldPremiumUser.setRecipes(newPremiumUser.getRecipes());
        if (newPremiumUser.getRecipebooks() != null) oldPremiumUser.setRecipebooks(newPremiumUser.getRecipebooks());
        if (newPremiumUser.getExpirationDate() != null) oldPremiumUser.setExpirationDate(newPremiumUser.getExpirationDate());
        oldPremiumUser.setFacebookId(newPremiumUser.getFacebookId());
        return function.apply(oldPremiumUser);
    }
}
