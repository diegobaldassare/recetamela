package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.restfb.*;
import com.restfb.exception.FacebookException;
import models.AuthToken;
import models.User;
import models.user.LoginData;
import org.apache.http.auth.AUTH;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.LoginService;
import services.UserService;
import util.ShaUtil;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matias Cicilia on 10-Sep-17.
 */

public class SecurityController extends Controller {

    final static String AUTH_TOKEN_HEADER = "X-TOKEN";

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
                JsonNode resp = generateAuthToken(user);
                return ok(resp);
            }
            else return unauthorized();
        } else {
            /* If we can't match this facebook ID with a user in our database, then this user is Signing Up for the first time */
            return ok(signUp(loginData));
        }
    }

    /* If User's been validated with facebook API, but hasn't been registered, then add him to DB */
    private JsonNode signUp(LoginData loginData) {
        User newUser = new User();
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
        return Json.toJson(token);
    }

    /* Logging out simply consists on deleting given server Authentication token */
    @Security.Authenticated(Secured.class)
    public Result logout() throws ExecutionException, InterruptedException {
        User user = getUser();

        LoginService.getInstance().findByHash(user.getAuthToken()).ifPresent(AuthToken::delete);
        user.setAuthToken(null);
        user.save();

        return ok();
    }

    private String fetchFacebookId(FacebookClient client) {
        String retrieved = client.fetchObject("me", String.class, Parameter.with("fields", "id"));
        return retrieved.split("\"")[3];
    }
}