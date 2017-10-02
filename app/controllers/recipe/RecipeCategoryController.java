package controllers.recipe;

import com.google.inject.Inject;
import controllers.authentication.Authenticate;
import models.recipe.RecipeBook;
import models.user.AdminUser;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.recipe.RecipeCategory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.recipe.RecipeCategoryService;

import java.util.List;

public class RecipeCategoryController extends Controller {

    private static Form<RecipeCategory> recipeCategoryForm;

    @Inject
    public RecipeCategoryController(FormFactory formFactory) {
        recipeCategoryForm =  formFactory.form(RecipeCategory.class);
    }

//    @Authenticate({AdminUser.class})
    public Result create() {
        final RecipeCategory category = recipeCategoryForm.bindFromRequest().get();
        category.save();
        return ok(Json.toJson(category));
    }

//    @Authenticate({AdminUser.class})
    public Result update(Long id) {
        return RecipeCategoryService.getInstance().get(id).map(category -> {
            final RecipeCategory newCategory = recipeCategoryForm.bindFromRequest().get();
            category.setName(newCategory.getName());
            category.update();
            return ok(Json.toJson(category));
        }).orElse(notFound());

    }

//    @Authenticate({FreeUser.class, PremiumUser.class, AdminUser.class})
    public Result get(Long id) {
        return RecipeCategoryService.getInstance().get(id)
                .map(recipeCategory -> ok(Json.toJson(recipeCategory))).orElse(notFound());
    }

//    @Authenticate({FreeUser.class, PremiumUser.class, AdminUser.class})
    public Result getAll() {
        final List<RecipeCategory> categories = RecipeCategoryService.getInstance().getFinder().all();
        return ok(Json.toJson(categories));
    }

//    @Authenticate({AdminUser.class})
    public Result delete(Long id) {
        return RecipeCategoryService.getInstance().get(id).map(recipeCategory -> {
            recipeCategory.delete();
            return ok();
        }).orElse(notFound());
    }
}
