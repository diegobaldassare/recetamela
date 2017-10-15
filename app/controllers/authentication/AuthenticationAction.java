package controllers.authentication;

import controllers.SecurityController;
import models.AuthToken;
import models.user.User;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.LoginService;
import services.user.UserService;

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

        if (authToken.get().startsWith("Bearer")) {
            /* Trim out <Type> to get the actual token */
            String token = authToken.get().substring("Bearer".length()).trim();
            Optional<User> userOptional = userService.findByAuthToken(token);
            if (!userOptional.isPresent()) {
                return CompletableFuture.completedFuture(unauthorized());
            }
            for (Class<? extends User> authorized: configuration.value()){
                Class<? extends User> userClass = userOptional.get().getClass();
                if(authorized.equals(userClass)){
                    if (validateToken(token, userOptional.get())) {
                /* Add user data to the context */
                        ctx.args.put("user", userOptional.get());
                        return delegate.call(ctx);
                    }
                }
            }
        }
        return CompletableFuture.completedFuture(unauthorized());
    }

    /* Validates the token */
    private boolean validateToken(String token, User userToValidate) {

        Optional<AuthToken> tokenObject = LoginService.getInstance().findByHash(token);

        /* Token on header will be valid if it matches the one on our DB, and if it hasn't expired yet */
        return tokenObject.isPresent() &&
                userToValidate.getAuthToken().equals(token) &&
                (tokenObject.get().getDate() + 80_000_000 > System.currentTimeMillis()) &&
                tokenObject.get().isValid();
    }
}
