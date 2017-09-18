package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.recipe.*;
import services.Service;

import java.util.List;

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
        if (input.getSteps() != null) {
            for (RecipeStep s : r.getSteps()) s.delete();
            r.setSteps(input.getSteps());
        }
        if (input.getVideoUrl() != null) r.setVideoUrl(input.getVideoUrl());
        if (input.getServings() != 0) r.setServings(input.getServings());
        if (input.getDuration() != 0) r.setDuration(input.getDuration());
    }

    public List<Recipe> search(RecipeSearchQuery q) {
        final List<Recipe> recipes = getFinder().all();
        recipes.removeIf(r -> {
            if (!r.getName().toLowerCase().contains(q.name)) return true;
            if (!q.difficulty.equals("0") && !String.valueOf(r.getDifficulty()).equals(q.difficulty)) return true;
            if (!r.getAuthor().getName().toLowerCase().contains(q.authorName)) return true;
            if (q.categoryNames.isEmpty()) return false;
            if (q.ingredientNames.isEmpty()) return false;

            for (final RecipeCategory c : r.getCategories())
                if (!q.categoryNames.contains(c.getName()))
                    return true;
            for (final Ingredient i : r.getIngredients())
                if (!q.ingredientNames.contains(i.getName()))
                    return true;

            return false;
        });
        return recipes;
    }
}
