package controllers.user;

import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.chefrequest.ChefRequest;
import models.notification.NotificationType;
import models.user.ChefUser;
import models.user.PremiumUser;
import models.user.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.user.ChefUserService;
import util.NotificationManager;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

public class ChefUserController extends BaseController {

    private static Form<ChefUser> userForm;

    @Inject
    public ChefUserController(FormFactory formFactory) {
        userForm = formFactory.form(ChefUser.class);
    }

    public Result updateChefUser(Long id) {
        ChefUser newChefUser = userForm.bindFromRequest().get();
        return ChefUserService.getInstance().get(id).map(user -> {
            if (newChefUser.getName() != null) user.setName(newChefUser.getName());
            if (newChefUser.getLastName() != null) user.setLastName(newChefUser.getLastName());
            if (newChefUser.getEmail() != null) user.setEmail(newChefUser.getEmail());
            if (newChefUser.getProfilePic() != null) user.setProfilePic(newChefUser.getProfilePic());
            if (newChefUser.getAuthToken() != null) user.setAuthToken(newChefUser.getAuthToken());
            if (newChefUser.getRecipes() != null) user.setRecipes(newChefUser.getRecipes());
            if (newChefUser.getRecipebooks() != null) user.setRecipebooks(newChefUser.getRecipebooks());
            if (newChefUser.getExpirationDate() != null) user.setExpirationDate(newChefUser.getExpirationDate());
            if (newChefUser.getNews() != null) user.setNews(newChefUser.getNews());
            if (newChefUser.getFollowedCategories() != null) user.setFollowedCategories(newChefUser.getFollowedCategories());
            user.setFacebookId(newChefUser.getFacebookId());
            user.update();
            return ok(Json.toJson(user));
        }).orElse(notFound());
    }

    public Result getChefUser(Long id) {
        final Optional<ChefUser> user = ChefUserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElse(notFound());
    }

    public Result getChefUsers() {
        return ok(Json.toJson(ChefUserService.getInstance().getFinder().all()));
    }

    public Result deleteChefUser(Long id) {
        return ChefUserService.getInstance().get(id).map(user -> {
            user.delete();
            return ok();
        }).orElse(notFound());
    }

    @Authenticate(PremiumUser.class)
    public Result sendChefRequest() {
        User user = getRequester();
        ChefRequest chefRequest = getBody(ChefRequest.class);
        chefRequest.setUser(user);
        try {
            checkFormat(chefRequest);
            chefRequest.save();
            NotificationManager.getInstance()
                    .notifyAdmins(user,
                            NotificationType.NEW_REQUEST,
                            "ha enviado una solicitud para ser chef",
                            user.getId().toString());
            return ok(Json.toJson(chefRequest));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    private void checkFormat(ChefRequest chefRequest) throws BadRequestException{
        if(chefRequest.getMedia() == null || !Objects.equals(chefRequest.getUser().getType(), "PremiumUser")) throw new BadRequestException(RequestError.BAD_FORMAT);
    }
}
