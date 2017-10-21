package controllers.recipe;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.recipe.RecipeCategory;
import models.user.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.recipe.RecipeCategoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeCategoryController extends BaseController {

    private static Form<RecipeCategory> recipeCategoryForm;

    @Inject
    public RecipeCategoryController(FormFactory formFactory) {
        recipeCategoryForm =  formFactory.form(RecipeCategory.class);
    }

    @Authenticate({AdminUser.class})
    public Result create() {
        final RecipeCategory category = recipeCategoryForm.bindFromRequest().get();
        category.save();
        return ok(Json.toJson(category));
    }

    @Authenticate({AdminUser.class})
    public Result update(Long id) {
        return RecipeCategoryService.getInstance().get(id).map(category -> {
            final RecipeCategory newCategory = recipeCategoryForm.bindFromRequest().get();
            category.setName(newCategory.getName());
            category.update();
            return ok(Json.toJson(category));
        }).orElse(notFound());

    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result get(Long id) {
        return RecipeCategoryService.getInstance().get(id)
                .map(recipeCategory -> ok(Json.toJson(recipeCategory))).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getByName(String name) {
        return RecipeCategoryService.getInstance().getByName(name)
                .map(recipeCategory -> ok(Json.toJson(recipeCategory))).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getAll() {
        final List<RecipeCategory> categories = RecipeCategoryService.getInstance().getFinder().all();
        return ok(Json.toJson(categories));
    }

    @Authenticate({AdminUser.class})
    public Result delete(Long id) {
        return RecipeCategoryService.getInstance().get(id)
                .map(this::deleteCategory)
                .orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    private Result deleteCategory(RecipeCategory recipeCategory) {
        SqlUpdate delete = Ebean.createSqlUpdate(
                "delete from recipe_recipe_category " +
                        "where recipe_category_id = :id");
        delete.setParameter("id", recipeCategory.getId());
        Ebean.execute(delete);
        recipeCategory.delete();
        return ok();
    }

    /**
     * This method returns all the results that match the search sent from the client.
     * @param query The received query parameter from the client
     * @return The list with tuples for the categories and a boolean that indicates whether the requester follows the category.
     */
    @Authenticate({ FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result search(String query) {
        User me = getRequester();
        final List<RecipeCategory> results = RecipeCategoryService.getInstance().search(query);

        List <CategoryListQuery> mappedResults = results.stream().map(e -> {
            if (me.getFollowedCategories().contains(e)) return new CategoryListQuery(e, true);
            else return new CategoryListQuery(e, false);
        }).collect(Collectors.toList());

        return ok(Json.toJson(mappedResults));
    }

    private class CategoryListQuery {
        RecipeCategory category;
        boolean followed;

        CategoryListQuery(RecipeCategory category, boolean followed) {
            this.category = category;
            this.followed = followed;
        }

        public RecipeCategory getCategory() {
            return category;
        }

        public boolean isFollowed() {
            return followed;
        }

        @Override
        public String toString() {
            return "CategoryListQuery{" +
                    "category=" + category.getName() +
                    ", followed=" + followed +
                    '}';
        }
    }
}
