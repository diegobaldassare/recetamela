package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.Media;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.RecipeInput;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.MediaService;
import services.Service;

import java.util.Optional;

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
    public Recipe save(RecipeInput input) throws BadRequestException {
        final Recipe recipe = new Recipe();
        recipe.setName(input.name);
        recipe.setDescription(input.description);
        setSteps(recipe, input.steps);
        recipe.setVideoUrl(input.videoUrl);
        recipe.setDifficulty(input.difficulty);
        setCategories(recipe, input.categoryNames);
        setIngredients(recipe, input.ingredientNames);
        final Media image = MediaService.getInstance().get(input.imageId).orElseThrow(() -> new BadRequestException(RequestError.BAD_FORMAT));
        recipe.setImage(image);
        // TODO recipe.setAuthor(?);
        recipe.save();
        return recipe;
    }

    public void setSteps(Recipe recipe, String[] steps) {
        recipe.setSteps(String.join("\n", steps));
    }

    public void setCategories(Recipe recipe, String[] names) throws BadRequestException {
        for (String name : names) {
            final Optional<RecipeCategory> categoryOpt = RecipeCategoryService.getInstance().getByName(name);
            final RecipeCategory category = categoryOpt.orElse(new RecipeCategory(name));
            if (!categoryOpt.isPresent()) category.save();
            recipe.getCategories().add(category);
        }
        if (recipe.getCategories().isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    public void setIngredients(Recipe recipe, String[] names) throws BadRequestException {
        for (String name : names) {
            final Optional<Ingredient> ingredientOpt = IngredientService.getInstance().getByName(name);
            final Ingredient ingredient = ingredientOpt.orElse(new Ingredient(name));
            if (!ingredientOpt.isPresent()) ingredient.save();
            recipe.getIngredients().add(ingredient);
        }
        if (recipe.getIngredients().isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
    }
}
