package controllers.user;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.AuthToken;
import models.Followers;
import models.News;
import models.payment.CreditCard;
import models.payment.Payment;
import models.recipe.Recipe;
import models.recipe.RecipeBook;
import models.recipe.RecipeCategory;
import models.user.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import server.exception.BadRequestException;
import services.LoginService;
import services.NewsService;
import services.payment.CreditCardService;
import services.payment.PaymentService;
import services.recipe.RecipeBookService;
import services.recipe.RecipeCategoryService;
import services.recipe.RecipeService;
import services.user.FollowerService;
import services.user.UserFormatter;
import services.user.UserService;
import services.user.UserValidator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserController extends BaseController {

    protected Form<User> userForm;

    @Inject
    public UserController(FormFactory formFactory) {
        userForm = formFactory.form(User.class);
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

    public Result getUsers() {
        List<User> users = UserService.getInstance().getFinder().all();
        return ok(Json.toJson(users));
    }

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
            recipes.forEach(recipe -> recipeService.deleteRecipe(recipe.getId()));

            /* Step 4: Delete all user RecipeBooks */
            final List<RecipeBook> recipeBooks = recipeBookService.getAllUserRecipeBook(r.getId());
            recipeBooks.forEach(RecipeBook::delete);

            /* Step 5: Delete all user News */
            final List<News> news = newsService.getNewsPublishedByuser(r.getId());
            news.forEach(News::delete);

            /* Step 5: Delete the User */
            LoginService.getInstance().findByHash(r.getAuthToken()).map(AuthToken::delete);
            r.delete();

            return ok();
        }).orElseGet(Results::notFound);
    }

    public Result getRecipeCategories(Long id) {
        return UserService.getInstance().get(id)
                .map(user -> ok(Json.toJson(user.getFollowedCategories())))
                .orElseGet(Results::notFound);
    }

    public Result getUnFollowedCategories(String id) {
        Optional<User> me = UserService.getInstance().get(Long.parseLong(id));
        if (!me.isPresent()) return notFound();
        final List<RecipeCategory> allCategories = RecipeCategoryService.getInstance().getFinder().all();
        final List<RecipeCategory> unFollowed = allCategories.stream()
                .filter(c -> !me.get().getFollowedCategories().contains(c))
                .collect(Collectors.toList());
        return ok(Json.toJson(unFollowed));
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result subscribeToCategory(long id) {
        User me = getRequester();
        Optional<RecipeCategory> category = RecipeCategoryService.getInstance().get(id);

        if (!category.isPresent()) return notFound();
        me.getFollowedCategories().add(category.get());
        category.get().getFollowers().add(me);
        category.get().update();
        me.update();

        return ok(Json.toJson(me));
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result unSubscribeToCategory(long id) {
        User me = getRequester();
        Optional<RecipeCategory> category = RecipeCategoryService.getInstance().get(id);

        if (!category.isPresent()) return notFound();
        me.getFollowedCategories().remove(category.get());
        category.get().getFollowers().remove(me);
        category.get().update();
        me.update();

        return ok(Json.toJson(category.get()));
    }

    public Result checkExpirationDate(Long id) {
        Optional<User> userOptional = UserService.getInstance().get(id);
        if (!userOptional.isPresent()) return notFound("User not Found");
        User user = userOptional.get();
        if (user.getType().equals("PremiumUser") && ((PremiumUser) user).isExpired()) return downgradeUser(user);
        if (user.getType().equals("ChefUser") && ((ChefUser) user).isExpired()) return downgradeUser(user);
        return ok(Json.toJson(new CheckExpirationDateResponse(user, false)));
    }

    private Result downgradeUser(User user) {
        user.setType("FreeUser");
        user.update();
        return ok(Json.toJson(new CheckExpirationDateResponse(user, true)));
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
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
