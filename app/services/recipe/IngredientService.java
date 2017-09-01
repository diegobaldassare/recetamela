package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.recipe.Ingredient;
import services.Service;

public class IngredientService extends Service<Ingredient> {
    private static IngredientService instance;

    private IngredientService(Finder<Long, Ingredient> finder) {
        super(finder);
    }

    public static IngredientService getInstance() {
        if (instance == null)
            instance = new IngredientService(new Finder<>(Ingredient.class));
        return instance;
    }

    public Ingredient getByName(String name) {
        return finder.where().eq("name", name).findUnique();
    }
}
