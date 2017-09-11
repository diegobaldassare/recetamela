package controllers;

import com.avaje.ebean.Model;
import com.google.inject.Inject;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import models.AuthToken;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import server.Constant;
import services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class UserController extends Controller {

    private static Form<User> userForm;

    @Inject
    public UserController(FormFactory formFactory) {
        userForm =  formFactory.form(User.class);
    }

    @Security.Authenticated(Secured.class)
    public Result me() throws ExecutionException, InterruptedException {
        User me = SecurityController.getUser();
        return ok(Json.toJson(me));
    }

    public Result registerAccessToken() {
        final AuthToken token = Json.fromJson(request().body().asJson(), AuthToken.class);
        FacebookClient client = new DefaultFacebookClient(token.getToken(), Version.VERSION_2_8);

        /* Using RestFB to get PP URL. Will move to another method later */
        //JsonObject picture = client.fetchObject("me/picture",
        //        JsonObject.class, Parameter.with("redirect","false"));
        //String picUrl = picture.get("url").asString();
        /* :) */

        /* Get 60-day valid access token */
        FacebookClient.AccessToken extendedAt = client.obtainExtendedAccessToken(Constant.APP_ID, Constant.APP_SECRET, token.getToken());
        token.setToken(extendedAt.getAccessToken());
        token.save();

        return ok(Json.toJson(token));
    }

    public Result createUser() {
        User user = userForm.bindFromRequest().get();
        if (!UserService.getInstance().get(user.getId()).isPresent()) {
            user.save();
            return ok(Json.toJson(user));
        }
        else return notFound();
    }

    public Result getUsers(){
        List<User> users = UserService.getInstance().getFinder().all();
        return ok(Json.toJson(users));
    }

    public Result deleteUser(Long id){
        Optional<User> user = UserService.getInstance().get(id);
        if (user.isPresent()){
            user.get().delete();
            return ok();
        }
        return notFound();
    }

    public Result getUser(Long id) {
        Optional<User> user = UserService.getInstance().get(id);
        if (user.isPresent()) return ok(Json.toJson(user.get()));
        return notFound();
    }
}
