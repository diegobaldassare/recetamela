package controllers.recipe;

import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.recipe.RecipeBook;
import models.user.AdminUser;
import models.user.ChefUser;
import models.user.PremiumUser;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import services.recipe.RecipeBookService;
import services.user.PremiumUserService;

import java.util.List;
import java.util.Optional;

public class RecipeBookController extends BaseController {

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result createRecipeBook() {
        final Optional<PremiumUser> premiumUserOptional = PremiumUserService.getInstance().get(getRequester().getId());
        return premiumUserOptional.map(user -> {
            final RecipeBook recipeBook = getBody(RecipeBook.class);
            recipeBook.setCreator(user);
            recipeBook.save();
            return ok(Json.toJson(recipeBook));
        }).orElse(notFound());
    }

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result updateRecipeBook(long id) {
        final RecipeBook newRecipeBook = getBody(RecipeBook.class);
        final Optional<RecipeBook> recipeBookOptional = RecipeBookService.getInstance().get(id);
        return recipeBookOptional.map(recipeBook -> {
            recipeBook.setName(newRecipeBook.getName());
            recipeBook.setRecipes(newRecipeBook.getRecipes());
            recipeBook.update();
            return ok(Json.toJson(recipeBook));
        }).orElse(notFound());
    }

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getAllRecipeBooks() {
        final List<RecipeBook> recipeBooks = RecipeBookService.getInstance().getFinder().all();
        return ok(Json.toJson(recipeBooks));
    }

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getRecipeBook(Long id) {
        final Optional<RecipeBook> recipe = RecipeBookService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getUserRecipeBooks() {
        final List<RecipeBook> recipeBooks = RecipeBookService.getInstance().getFinder().query()
                .where()
                .eq("creator", getRequester())
                .findList();
        return ok(Json.toJson(recipeBooks));
    }

    @Authenticate({PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result deleteRecipeBook(Long id) {
        final Optional<RecipeBook> recipe = RecipeBookService.getInstance().get(id);
        return recipe.map(r -> {
            r.delete();
            return ok();
        }).orElse(notFound());
    }
}
