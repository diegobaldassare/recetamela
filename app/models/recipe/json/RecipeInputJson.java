package models.recipe.json;

import models.recipe.Recipe;

public class RecipeInputJson {
    private Recipe recipe;

    public RecipeInputJson(Recipe recipe) {
        this.recipe = recipe;
    }

    public long getId() {
        return recipe.id;
    }

    public String getName() {
        return recipe.getName();
    }
}
