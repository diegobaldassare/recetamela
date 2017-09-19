package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.Media;
import models.recipe.Recipe;
import models.recipe.RecipeStep;
import services.MediaService;
import services.Service;

import java.util.ArrayList;
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
        if (input.getSteps() != null) setSteps(r, input.getSteps());
        if (input.getVideoUrl() != null) r.setVideoUrl(input.getVideoUrl());
        if (input.getServings() != 0) r.setServings(input.getServings());
        if (input.getDuration() != 0) r.setDuration(input.getDuration());
    }

    private void setSteps(Recipe r, List<RecipeStep> steps) {
        final List<Media> imagesToDelete = new ArrayList<>();
        for (final RecipeStep s : r.getSteps()) {
            if (s.getImage() != null) imagesToDelete.add(s.getImage());
            s.setImage(null);
            s.delete();
        }
        for (final RecipeStep s : steps)
            if (s.getImage() != null && imagesToDelete.contains(s.getImage()))
                imagesToDelete.remove(s.getImage());
        for (final Media i : imagesToDelete) MediaService.getInstance().delete(i.getId());
        r.setSteps(steps);
    }
}
