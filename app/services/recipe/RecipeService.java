package services.recipe;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.SqlUpdate;
import models.Comment;
import models.Media;
import models.recipe.Recipe;
import models.recipe.RecipeStep;
import services.CommentService;
import services.MediaService;
import models.recipe.*;
import services.NewsService;
import services.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeService extends Service<Recipe> {

    private static RecipeService instance;

    private RecipeService(Finder<Long, Recipe> finder) {
        super(finder);
    }

    public static RecipeService getInstance() {
        if (instance == null) instance = new RecipeService(new Finder<>(Recipe.class));
        return instance;
    }

    public void modify(Recipe r, Recipe input) {
        if (input.getName() != null) r.setName(input.getName());
        if (input.getDescription() != null) r.setDescription(input.getDescription());
        if (input.getCategories() != null) r.setCategories(input.getCategories());
        if (input.getIngredients() != null) {
            for (final IngredientFormula i : r.getIngredients()) i.delete();
            r.setIngredients(input.getIngredients());
        }
        if (input.getImages() != null) r.setImages(input.getImages());
        if (input.getDifficulty() != 0) r.setDifficulty(input.getDifficulty());
        if (input.getSteps() != null) setSteps(r, input.getSteps());
        if (input.getVideoUrl() != null) r.setVideoUrl(input.getVideoUrl());
        if (input.getServings() != 0) r.setServings(input.getServings());
        if (input.getDuration() != 0) r.setDuration(input.getDuration());
    }

    public List<Recipe> getUserRecipes(long userId) {
        return getFinder().where()
                .eq("author_id", userId)
                .findList();
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
        for (final Media i : imagesToDelete) MediaService.getInstance().delete(i);
        r.setSteps(steps);
    }

    public List<Recipe> search(RecipeSearchQuery q) {
        final List<Recipe> recipes = getFinder().all();
        recipes.removeIf(r -> {
            if (!r.getName().toLowerCase().contains(q.name)) return true;
            if (!q.difficulty.equals("0") && !String.valueOf(r.getDifficulty()).equals(q.difficulty)) return true;
            final String author = (r.getAuthor().getName() + r.getAuthor().getLastName()).toLowerCase().replaceAll("[^a-z ]", "");
            boolean authorMatch = false;
            for (final String namePart : q.author.split(" ")) {
                authorMatch = author.contains(namePart);
                if (authorMatch) break;
            }
            if (!authorMatch) return true;
            if (!q.categories.isEmpty()) {
                final List<String> categories = new ArrayList<>();
                r.getCategories().forEach(c -> categories.add(c.getName()));
                if (!categories.containsAll(q.categories)) return true;
            }
            if (!q.ingredients.isEmpty()) {
                final List<String> ingredients = new ArrayList<>();
                r.getIngredients().forEach(i -> ingredients.add(i.getIngredient().getName()));
                if (!ingredients.containsAll(q.ingredients)) return true;
            }
            return false;
        });
        return recipes;
    }

    public void addComment(Recipe recipe, Comment comment) {
        recipe.getComments().add(comment);
        recipe.update();
    }

    public void delete(Recipe r) {
        final MediaService mediaService = MediaService.getInstance();
        final List<Media> images = new ArrayList<>(r.getImages());
        deleteStepsImages(r, mediaService);
        deleteFromRecipeBooks(r);
        NewsService.getInstance().getNewsForRecipe(r.getId()).delete();
        deleteComments(r.getId());
        r.delete();
        images.forEach(mediaService::delete);
    }

    private void deleteFromRecipeBooks(Recipe recipe) {
        RecipeBookService.getInstance().deleteRecipe(recipe);
    }

    private void deleteComments(Long recipeID) {
        CommentService.getInstance().deleteRecipeComments(recipeID);
    }

    private void deleteStepsImages(Recipe recipe, MediaService mediaService) {
        recipe.getSteps().stream()
                .map(RecipeStep::getImage)
                .filter(Objects::nonNull)
                .forEach(mediaService::deleteFile);
    }

    public List<Recipe> getRecipesForCategory(RecipeCategory category) {
        List<Recipe> result = getFinder().all();
        result.removeIf(recipe -> !recipe.getCategories().contains(category));
        return result;
    }

    public void deleteChefLikes(Long chefID) {
        SqlUpdate delete = Ebean.createSqlUpdate(
                "delete from recipe_user " +
                        "where user_id = :id");
        delete.setParameter("id", chefID);
        Ebean.execute(delete);
    }

    public List<Recipe> getTopRankingRecipes() {
        return getFinder().where()
                .orderBy().desc("rating")
                .setMaxRows(3)
                .findList();
    }
}
