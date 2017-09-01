package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.media.Media;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.json.RecipeInputJson;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.MediaService;
import services.Service;

import java.util.Collection;
import java.util.List;

public class RecipeService extends Service<Recipe> {

    private static RecipeService instance;

    private RecipeService(Finder<Long, Recipe> finder){
        super(finder);
    }

    public static RecipeService getInstance(){
        if (instance == null) instance = new RecipeService(new Finder<>(Recipe.class));
        return instance;
    }

    /**
     * Persists recipe into database. Receives the recipe input from the request with
     * some verifications already done.
     * @param input Recipe input from the request.
     * @return A persisted Recipe model instance.
     * @throws BadRequestException If the request input is invalid.
     */
    public Recipe save(RecipeInputJson input) throws BadRequestException {
        final Recipe recipe = new Recipe();
        recipe.setName(input.name);
        recipe.setDescription(input.description);
        recipe.setSteps(input.steps);
        recipe.setVideoUrl(input.videoUrl);
        recipe.setDifficulty(input.difficulty);
        for (Long id : input.categoryIds) {
            final RecipeCategory category = RecipeCategoryService.getInstance().get(id);
            if (category == null) continue;
            recipe.getCategories().add(category);
        }
        if (recipe.getCategories().isEmpty())
            throw new BadRequestException(RequestError.BAD_FORMAT);
        for (String name : input.ingredientNames) {
            Ingredient ingredient = IngredientService.getInstance().getByName(name);
            if (ingredient == null) {
                ingredient = new Ingredient(name);
                ingredient.save();
            }
            recipe.getIngredients().add(ingredient);
        }
        if (recipe.getIngredients().isEmpty())
            throw new BadRequestException(RequestError.BAD_FORMAT);
        final Media image = MediaService.getInstance().get(input.imageId);
        if (image == null) throw new BadRequestException(RequestError.BAD_FORMAT);
        recipe.setImage(image);
        // TODO Check if author exists and insert into recipe
        recipe.save();
        return recipe;
    }

    /**
     * Get all recipes.
     * @param limit Maximum number of returned recipes.
     * @return A list of recipes that must not exceed the limit.
     */
    public List<Recipe> getAll(int limit){
        return finder.all();
    }

    /**
     * Get recipes that belong to the received categories.
     * @param categories Categories that returned recipes must belong to.
     * @param limit Maximum number of returned recipes.
     * @return Recipes that belong to the received categories.
     */
    public List<Recipe> getByCategory(Collection<RecipeCategory> categories, int limit) {
        return finder.where().eq("categories", categories).findList(); // TODO should be .contains()
    }

    /**
     * Get recipes that contain received ingredients.
     * @param ingredients Ingredients that must contain the returned recipes.
     * @param limit Maximum number of returned recipes.
     * @return Recipes that contain received ingredients.
     */
    public List<Recipe> getByIngredients(Collection<Ingredient> ingredients, int limit) {
        return finder.where().eq("ingredients", ingredients).findList(); // TODO should be .contains()
    }
}
