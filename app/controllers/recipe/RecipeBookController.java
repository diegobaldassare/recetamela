package controllers.recipe;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.recipe.RecipeBook;
import models.user.PremiumUser;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import services.recipe.RecipeBookService;
import services.user.PremiumUserService;

import java.util.List;
import java.util.Optional;

public class RecipeBookController extends BaseController {

    private static Form<RecipeBook> recipeBookForm;

    @Inject
    public RecipeBookController(FormFactory formFactory) {
        recipeBookForm =  formFactory.form(RecipeBook.class);
    }

    @Authenticate({PremiumUser.class})
    public Result createRecipeBook() {
        final Optional<PremiumUser> premiumUserOptional = PremiumUserService.getInstance().get(getRequester().getId());
        return premiumUserOptional.map(user -> {
            final RecipeBook recipeBook = recipeBookForm.bindFromRequest().get();
            recipeBook.setCreator(user);
            recipeBook.save();
            return ok(Json.toJson(recipeBook));
        }).orElse(notFound());
    }

    @Authenticate({PremiumUser.class})
    public Result updateRecipeBook(long id) {
        final RecipeBook newRecipeBook = recipeBookForm.bindFromRequest().get();
        final Optional<RecipeBook> recipeBookOptional = RecipeBookService.getInstance().get(id);
        return recipeBookOptional.map(recipeBook -> {
            recipeBook.setName(newRecipeBook.getName());
            recipeBook.setRecipes(newRecipeBook.getRecipes());
            recipeBook.update();
            return ok(Json.toJson(recipeBook));
        }).orElse(notFound());
    }

    @Authenticate({PremiumUser.class})
    public Result getAllRecipeBooks() {
        final List<RecipeBook> recipeBooks = RecipeBookService.getInstance().getFinder().all();
        return ok(Json.toJson(recipeBooks));
    }

    @Authenticate({PremiumUser.class})
    public Result getRecipeBook(Long id) {
        final Optional<RecipeBook> recipe = RecipeBookService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }

    @Authenticate({PremiumUser.class})
    public Result getUserRecipeBooks() {
        final List<RecipeBook> recipeBooks = RecipeBookService.getInstance().getFinder().query()
                .where()
                .eq("creator", getRequester())
                .findList();
        return ok(Json.toJson(recipeBooks));
    }

    @Authenticate({PremiumUser.class})
    public Result deleteRecipeBook(Long id) {
        final Optional<RecipeBook> recipe = RecipeBookService.getInstance().get(id);
        return recipe.map(r -> {
            r.delete();
            return ok();
        }).orElse(notFound());
    }
}
