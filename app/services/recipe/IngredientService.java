package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.recipe.Ingredient;
import services.Service;

import java.util.Optional;

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

    public Optional<Ingredient> getByName(String name) {
        return Optional.ofNullable(finder.where().eq("name", name).findUnique());
    }
}
