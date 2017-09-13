package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.recipe.*;
import services.Service;

public class RecipeService extends Service<Recipe> {

    private static RecipeService instance;

    private RecipeService(Finder<Long, Recipe> finder){
        super(finder);
    }

    public static RecipeService getInstance() {
        if (instance == null) instance = new RecipeService(new Finder<>(Recipe.class));
        return instance;
    }

    public void modify(Recipe r, Recipe input) {
        if (input.getName() != null) r.setName(input.getName());
        if (input.getDescription() != null) r.setName(input.getName());
        if (input.getCategories() != null) r.setCategories(input.getCategories());
        if (input.getIngredients() != null) r.setIngredients(input.getIngredients());
        if (input.getImages() != null) r.setImages(input.getImages());
        if (input.getDifficulty() != 0) r.setDifficulty(input.getDifficulty());
        if (input.getSteps() != null) r.setSteps(input.getSteps());
        if (input.getVideoUrl() != null) r.setVideoUrl(input.getVideoUrl());
    }
}
