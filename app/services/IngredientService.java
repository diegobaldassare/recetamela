package services;

import com.avaje.ebean.Model;
import models.recipe.Ingredient;

public class IngredientService extends Service<Ingredient> {
    private static IngredientService instance;

    private IngredientService(Model.Finder<Long, Ingredient> finder) {
        super(finder);
    }

    public static IngredientService getInstance() {
        return instance;
    }

    public Ingredient getByName(String name) {
        return finder.where().eq("name", name).findUnique();
    }
}
