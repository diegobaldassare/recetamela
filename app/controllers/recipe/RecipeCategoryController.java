package controllers.recipe;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import com.google.inject.Inject;
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
        return RecipeCategoryService.getInstance().get(id).map(recipeCategory -> deleteCategory(recipeCategory, 0)
//                RecipeCategoryService.getInstance().getByName("sin categoria").map(defaultCategory ->
//                        deleteCategory(recipeCategory, defaultCategory.getId()))
//                .orElseGet(() -> {
//                    RecipeCategory defaultCategory = new RecipeCategory();
//                    defaultCategory.setName("sin categoria");
//                    defaultCategory.save();
//                    return deleteCategory(recipeCategory, defaultCategory.getId());
//        })
        ).orElse(notFound());
    }

    private Result deleteCategory(RecipeCategory recipeCategory, long defaultCategoryId) {
        SqlUpdate delete = Ebean.createSqlUpdate(
                "delete from recipe_recipe_category " +
                        "where recipe_category_id = :id");
        delete.setParameter("id", recipeCategory.getId());
        Ebean.execute(delete);

//        Ebean.createSqlQuery("select id " +
//                "from recipe " +
//                "left join recipe_recipe_category " +
//                "on (id = recipe_id) " +
//                "where (recipe_id is null)")
//                .findList()
//                .forEach(recipe -> {
//                    SqlUpdate insert = Ebean.createSqlUpdate(
//                            "insert into recipe_recipe_category " +
//                                    "values (:recipe, :category)");
//                    insert.setParameter("recipe", recipe.getInteger("id"));
//                    insert.setParameter("category", defaultCategoryId);
//                    Ebean.execute(insert);
//                });

        recipeCategory.delete();
        return ok();
    }
}
