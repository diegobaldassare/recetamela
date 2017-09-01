package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.recipe.RecipeCategory;
import services.Service;

public class RecipeCategoryService extends Service<RecipeCategory> {
    private static RecipeCategoryService instance;

    private RecipeCategoryService(Finder<Long, RecipeCategory> finder) {
        super(finder);
    }

    public static RecipeCategoryService getInstance() {
        if (instance == null)
            instance = new RecipeCategoryService(new Finder<>(RecipeCategory.class));
        return instance;
    }
}
