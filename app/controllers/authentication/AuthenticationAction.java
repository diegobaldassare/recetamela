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
        Logger.debug("Got token: " + authToken.get());
        Logger.debug("Secured call to "+ctx.request().method()+ " " +ctx.request().path());

        if (authToken.get().startsWith("Bearer")) {

            System.out.println("Token starts with bearer! ");
            /* Trim out <Type> to get the actual token */
            String token = authToken.get().substring("Bearer".length()).trim();

            System.out.println("Token is: " + authToken.get());

            Optional<User> userOptional = userService.findByAuthToken(token);

            System.out.print("We found user: ");
            userOptional.ifPresent(System.out::println);

            if (userOptional.isPresent() && validateToken(token, userOptional.get())) {
                /* Add user data to the context */
                System.out.println("Validated token! ");
                Logger.debug("Adding user to context as: " + userOptional.get());
                ctx.args.put("user", userOptional.get());
                return delegate.call(ctx);
            }

        }
        return CompletableFuture.completedFuture(unauthorized());
    }

    /* Validates the token */
    private boolean validateToken(String token, User userToValidate) {

        Optional<AuthToken> tokenObject = LoginService.getInstance().findByHash(token);

        Logger.debug("Token is present: " + tokenObject.isPresent());
        Logger.debug("User and token: " + userToValidate.getAuthToken().equals(token));
        Logger.debug("Expired: " + (tokenObject.get().getDate() + 80_000_000 > System.currentTimeMillis()));
        /* Token on header will be valid if it matches the one on our DB, and if it hasn't expired yet */
        return tokenObject.isPresent() &&
                userToValidate.getAuthToken().equals(token) &&
                (tokenObject.get().getDate() + 80_000_000 > System.currentTimeMillis());
    }
}
