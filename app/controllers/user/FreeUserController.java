package controllers.user;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxRunnable;
import com.google.inject.Inject;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.user.FreeUserService;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class FreeUserController extends Controller {

    private static Form<FreeUser> userForm;

    @Inject
    public FreeUserController(FormFactory formFactory) {
        userForm =  formFactory.form(FreeUser.class);
    }

    public Result createFreeUser() {
        FreeUser user = userForm.bindFromRequest().get();
        user.save();
        return ok(Json.toJson(user));
    }

    public Result upgradeFreeUser(Long id) {
        Optional<FreeUser> freeUserOptional = FreeUserService.getInstance().get(id);
        if (!freeUserOptional.isPresent()) return notFound();
        FreeUser user = freeUserOptional.get();
//        user.delete();
        PremiumUser premiumUser = new PremiumUser(user.getName(), user.getLastName(), user.getEmail(), user.getProfilePic());
        premiumUser.setId(user.getId());
        premiumUser.setType("PremiumUser");
        premiumUser.setFacebookId(user.getFacebookId());
        premiumUser.setAuthToken(user.getAuthToken());
        premiumUser.setExpirationDate(LocalDate.now().plus(Period.ofMonths(1)));
//        premiumUser.setCreditCards(user.getCreditCards());
        System.out.println(premiumUser.getId());
        premiumUser.update();
        return ok(Json.toJson(premiumUser));
    }

    public Result updateFreeUser(Long id) {
        try {
            return updateFreeUser(id, user -> {
                user.update();
                return ok(Json.toJson(user));
            });
        } catch (NoSuchObjectException err) {
            return notFound(err.getMessage());
        }
    }

    public Result getFreeUser(Long id) {
        final Optional<FreeUser> user = FreeUserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElseGet(Results::notFound);
    }

    public Result getFreeUsers(){
        List<FreeUser> users = FreeUserService.getInstance().getFinder().all();
        return ok(Json.toJson(users));
    }

    public Result deleteFreeUser(Long id){
        Optional<FreeUser> user = FreeUserService.getInstance().get(id);
        if (user.isPresent()){
            user.get().delete();
            return ok();
        }
        return notFound();
    }

    private Result updateFreeUser(Long id, Function<User, Result> function) throws NoSuchObjectException {
        FreeUser newFreeUser = userForm.bindFromRequest().get();
        Optional<FreeUser> optionalFreeUser = FreeUserService.getInstance().get(id);
        FreeUser oldFreeUser;
        if(optionalFreeUser.isPresent()) oldFreeUser = optionalFreeUser.get();
        else throw new NoSuchObjectException("The user was not found");
        if (newFreeUser.getName() != null) oldFreeUser.setName(newFreeUser.getName());
        if (newFreeUser.getLastName() != null) oldFreeUser.setLastName(newFreeUser.getLastName());
        if (newFreeUser.getEmail() != null) oldFreeUser.setEmail(newFreeUser.getEmail());
        if (newFreeUser.getProfilePic() != null) oldFreeUser.setProfilePic(newFreeUser.getProfilePic());
        if (newFreeUser.getAuthToken() != null) oldFreeUser.setAuthToken(newFreeUser.getAuthToken());
//        if (newFreeUser.getCreditCards() != null) oldFreeUser.setCreditCards(newFreeUser.getCreditCards());
        oldFreeUser.setFacebookId(newFreeUser.getFacebookId());
        return function.apply(oldFreeUser);
    }
}
