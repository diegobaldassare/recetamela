package services;

import com.avaje.ebean.Model;
import models.recipe.RecipeCategory;

public class RecipeCategoryService extends Service<RecipeCategory> {
    private static RecipeCategoryService instance;

    private RecipeCategoryService(Model.Finder<Long, RecipeCategory> finder) {
        super(finder);
    }

    public static RecipeCategoryService getInstance() {
        return instance;
    }
}
