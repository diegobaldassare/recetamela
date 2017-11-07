package controllers.user;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.AuthToken;
import models.Followers;
import models.News;
import models.chefrequest.ChefRequest;
import models.payment.CreditCard;
import models.payment.Payment;
import models.recipe.Recipe;
import models.recipe.RecipeBook;
import models.user.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.exception.BadRequestException;
import services.ChefRequestService;
import services.CommentService;
import services.LoginService;
import services.NewsService;
import services.payment.CreditCardService;
import services.payment.PaymentService;
import services.recipe.RecipeBookService;
import services.recipe.RecipeCategoryService;
import services.recipe.RecipeRatingService;
import services.recipe.RecipeService;
import services.user.FollowerService;
import services.user.UserFormatter;
import services.user.UserService;
import services.user.UserValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

public class UserController extends BaseController {

    protected Form<User> userForm;

    @Inject
    public UserController(FormFactory formFactory) {
        userForm = formFactory.form(User.class);
    }

    public UserController() {
    }

    public Result createUser() {
        User user = userForm.bindFromRequest().get();
        user.save();
        return ok(Json.toJson(user));
    }

    public Result updateUser(Long id) {
        User newUser = userForm.bindFromRequest().get();
        return UserService.getInstance().get(id).map(user -> {
            user.setName(newUser.getName());
            user.setLastName(newUser.getLastName());
            user.setEmail(newUser.getEmail());
            return ok(Json.toJson(user));
        }).orElse(notFound());
    }

    public Result getUser(Long id) {
        final Optional<User> user = UserService.getInstance().get(id);
        return user.map(u -> ok(Json.toJson(u))).orElseGet(Results::notFound);
    }

    public Result getUsers(String name) {
        final List<User> users = UserService.getInstance().findAllByName(name);
        return ok(Json.toJson(users));
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result deleteUser(Long id) {
        final CreditCardService cardService = CreditCardService.getInstance();
        final PaymentService paymentService = PaymentService.getInstance();
        final FollowerService followerService = FollowerService.getInstance();
        final RecipeService recipeService = RecipeService.getInstance();
        final RecipeBookService recipeBookService = RecipeBookService.getInstance();
        final NewsService newsService = NewsService.getInstance();
        Optional<User> user = UserService.getInstance().get(id);
        return user.map(r -> {
            //* Step 1: Delete all credit cards associated with the User *//*
            final List<CreditCard> userCreditCards = cardService.getAllUserCreditCards(r.getId());
            userCreditCards.forEach(c -> {
                final List<Payment> payments = paymentService.getAllPaymentsWithCreditCard(c.getId());
                payments.forEach(Payment::delete);
                c.delete();
            });

            /* Step 2: Delete records of followers/following */
            List<Followers> followers = followerService.getFollowers(r.getId());
            List<Followers> following = followerService.getFollowing(r.getId());
            followers.forEach(Followers::delete);
            following.forEach(Followers::delete);

            /* Step 3: Delete all user Recipes */
            final List<Recipe> recipes = recipeService.getUserRecipes(r.getId());
            recipes.forEach(recipeService::delete);

            /* Step 4: Delete all user Comments*/
            CommentService.getInstance().deleteUserComments(r.getId());

            /* Step 5: Delete all user RecipeBooks */
            final List<RecipeBook> recipeBooks = recipeBookService.getAllUserRecipeBook(r.getId());
            recipeBooks.forEach(RecipeBook::delete);

            /* Step 6: Delete all user News */
            final List<News> news = newsService.getNewsPublishedByUser(r.getId());
            news.forEach(News::delete);

            /* Step 7: Delete all user chef requests */
            final List<ChefRequest> requests = ChefRequestService.getInstance().getRequestsByUser(r.getId());
            requests.forEach(ChefRequest::delete);

            /* Step 8: Delete rows from user-recipeCategory table */
            RecipeCategoryService.getInstance().deleteUser(r.getId());

            /* Step 9: Delete rows from recipe-ratings */
            RecipeRatingService.getInstance().deleteUserRatings(r.getId());

            /* Step 10: Delete the User */
            LoginService.getInstance().findByHash(r.getAuthToken()).map(AuthToken::delete);
            r.delete();

            return ok();
        }).orElseGet(Results::notFound);
    }

    public Result checkExpirationDate(Long id) {
        return UserService.getInstance().get(id).map(user -> {
            if (user.getType().equals("PremiumUser") && ((PremiumUser) user).isExpired()) return downgradeUser(user);
            if (user.getType().equals("ChefUser") && ((ChefUser) user).isExpired()) return downgradeUser(user);
            return ok(Json.toJson(new CheckExpirationDateResponse(user, false)));
        }).orElse(notFound("User not Found"));
    }

    private Result downgradeUser(User user) {
        user.setType("FreeUser");
        user.update();
        return ok(Json.toJson(new CheckExpirationDateResponse(user, true)));
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result upgradeToPremium(Long id) {
        return UserService.getInstance().get(id).map(user -> {
            PremiumUser premiumUser = buildPremiumUser(user);
            premiumUser.update();
            return ok(Json.toJson(premiumUser));
        }).orElse(notFound());
    }

    public Result upgradeToChef(Long id) {
        return UserService.getInstance().get(id).map(user -> {
            ChefUser chefUser = buildChefUser(user);
            chefUser.update();
            return ok(Json.toJson(chefUser));
        }).orElse(notFound());
    }

    protected PremiumUser buildPremiumUser(User user) {
        PremiumUser premiumUser = new PremiumUser(user.getName(), user.getLastName(), user.getEmail(), user.getProfilePic());
        premiumUser.setId(user.getId());
        premiumUser.setType("PremiumUser");
        premiumUser.setFacebookId(user.getFacebookId());
        premiumUser.setAuthToken(user.getAuthToken());
        premiumUser.setExpirationDate(LocalDate.now().plus(Period.ofMonths(1)));
        return premiumUser;
    }

    protected ChefUser buildChefUser(User user) {
        ChefUser chefUser = new ChefUser();
        chefUser.setName(user.getName());
        chefUser.setLastName(user.getLastName());
        chefUser.setEmail(user.getEmail());
        chefUser.setProfilePic(user.getProfilePic());
        chefUser.setId(user.getId());
        chefUser.setType("ChefUser");
        chefUser.setFacebookId(user.getFacebookId());
        chefUser.setAuthToken(user.getAuthToken());
        chefUser.setExpirationDate(LocalDate.now().plus(Period.ofMonths(1)));
        return chefUser;
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result modify(long id) {
        final Optional<User> user = UserService.getInstance().get(id);
        if (!user.isPresent()) return notFound();
        final User u = getBody(User.class);
        UserFormatter.format(u);
        try {
            UserValidator.validateNotNullFields(u);
            UserService.getInstance().modify(user.get(), u);
            user.get().save();
            return ok(Json.toJson(user.get()));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }
}
