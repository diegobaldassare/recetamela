package controllers.authentication;

import controllers.SecurityController;
import models.AuthToken;
import models.User;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.LoginService;
import services.UserService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AuthenticationAction extends Action<Authenticate> {


    private UserService userService = UserService.getInstance();
    /**
     *  Retrieves the username from the HTTP context;
     *
     *  When a controller is called and is annotated with @Secured this method will intercept the call,
     *  validate the auth token and add the User data to the context
     *
     *  For @Security.Authenticated(Secured.class) methods the API expects a header:
     *    X-AUTH-TOKEN : Bearer [Authtoken given at login]
     *
     * @param ctx Http request call information
     * @return The Username String, null if the user is not authenticated.
     */

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        ctx.request().getHeader(SecurityController.AUTH_TOKEN_HEADER);

        Optional<String> authToken = Optional.ofNullable(ctx.request().getHeader(SecurityController.AUTH_TOKEN_HEADER));

        if (!authToken.isPresent()) return CompletableFuture.completedFuture(unauthorized());
        Logger.debug("Secured call to "+ctx.request().method()+ " " +ctx.request().path());

        if (authToken.get().startsWith("Bearer")) {
            /* Trim out <Type> to get the actual token */
            String token = authToken.get().substring("Bearer".length()).trim();
            Optional<User> userOptional = userService.findByAuthToken(token);

            Logger.debug("Secured call made by: " + (userOptional.isPresent() ? userOptional.get().getName(): "undefined user") );

            if (userOptional.isPresent() && validateToken(token, userOptional.get())) {
                /* Add user data to the context */
                Logger.debug("Secured call validated, adding " + userOptional.get().getName() + " to context");
                ctx.args.put("user", userOptional.get());
                return delegate.call(ctx);
            }

        }
        return CompletableFuture.completedFuture(unauthorized());
    }

    /* Validates the token */
    private boolean validateToken(String token, User userToValidate) {

        Optional<AuthToken> tokenObject = LoginService.getInstance().findByHash(token);

        /* Token on header will be valid if it matches the one on our DB, and if it hasn't expired yet */
        boolean validated = tokenObject.isPresent() &&
                userToValidate.getAuthToken().equals(token) &&
                (tokenObject.get().getDate() + 80_000_000 > System.currentTimeMillis()) &&
                tokenObject.get().isValid();
        return validated;
    }
}
