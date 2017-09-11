package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import controllers.authentication.Authenticate;
import models.AuthToken;
import models.FreeUser;
import models.PremiumUser;
import models.User;
import models.user.LoginData;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.FreeUserService;
import services.LoginService;
import services.UserService;
import util.ShaUtil;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matias Cicilia on 10-Sep-17.
 */

public class SecurityController extends Controller {

    public final static String AUTH_TOKEN_HEADER = "X-TOKEN";

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

    public Result login() {
        /* Me llega data, yo con esa data busco al usuario en mi base de datos. */
        LoginData loginData = Json.fromJson(request().body().asJson(), LoginData.class);
        Optional<User> userOptional = UserService.getInstance().findByFacebookId(loginData.getId());
        String retrievedId;

        try {
        /* Con el accessToken que el usuario dice que recibió de facebook, busco su ID de facebook con FB Graph */
            FacebookClient client = new DefaultFacebookClient(loginData.getAccessToken(), Version.VERSION_2_8);
            retrievedId = fetchFacebookId(client);
        } catch (FacebookException e) {
            e.printStackTrace();
            Logger.debug("An error occurred retrieving FB object");
            return unauthorized();
        }

        /* If User's been found, then check his facebook token and log him in */
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            /* If the accessToken the user claims facebook gave him checks out, then we can log him in. */
            if ( Long.toString(user.getFacebookId()).equals(retrievedId) ) {
                Logger.debug("Logged in as user " + user.getName() + " id " + user.getFacebookId());
                JsonNode resp = generateAuthToken(user);
                return ok(resp);
            }
            else return unauthorized();
        } else {
            /* If we can't match this facebook ID with a user in our database, then this user is Signing Up for the first time */
            return ok(signUp(loginData));
        }
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getLoggedData() {
        User me = getUser();
        return ok(Json.toJson(me));
    }

    /* If User's been validated with facebook API, but hasn't been registered, then add him to DB */
    private JsonNode signUp(LoginData loginData) {
        User newUser = new FreeUser();
        newUser.setEmail(loginData.getEmail());
        newUser.setFacebookId(loginData.getId());
        newUser.setName(loginData.getName().split(" ")[0]);
        newUser.setLastName(loginData.getName().split(" ")[1]);
        newUser.save();
        /* After used is saved, log him in */
        return generateAuthToken(newUser);
    }

    private JsonNode generateAuthToken(User user) {
        Date date = new Date();
        String hash = ShaUtil.sha256(user.getName() + date.getTime());
        AuthToken token = new AuthToken(hash, date.getTime(), user.getFacebookId());
        user.setAuthToken(token.getToken());
        user.save();
        token.save();
        Logger.debug("Generated token " + token.getToken() + " for user " + user.getName());
        return Json.toJson(token);
    }

    /* Logging out simply consists on deleting given server Authentication token */
    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result logout() throws ExecutionException, InterruptedException {
        User user = getUser();

        LoginService.getInstance().findByHash(user.getAuthToken()).ifPresent(AuthToken::delete);
        user.setAuthToken(null);
        user.update();

        return ok();
    }

    private String fetchFacebookId(FacebookClient client) {
        String retrieved = client.fetchObject("me", String.class, Parameter.with("fields", "id"));
        return retrieved.split("\"")[3];
    }
}