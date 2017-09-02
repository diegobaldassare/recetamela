package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.media.Media;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.json.RecipeInputJson;
import org.apache.commons.lang3.StringUtils;
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
    public Recipe save(RecipeInputJson input) throws BadRequestException {
        final Recipe recipe = new Recipe();
        recipe.setName(capitalize(input.name));
        recipe.setDescription(capitalize(input.description));
        recipe.setSteps(input.steps.toLowerCase());
        recipe.setVideoUrl(input.videoUrl);
        recipe.setDifficulty(input.difficulty);
        for (String name : input.categoryNames) {
            if (!StringUtils.isAlphaSpace(name))
                throw new BadRequestException(RequestError.BAD_FORMAT);
            final String nameLowerCase = name.toLowerCase();
            final Optional<RecipeCategory> categoryOpt = RecipeCategoryService
                    .getInstance()
                    .getByName(nameLowerCase);
            final RecipeCategory category = categoryOpt
                    .orElse(new RecipeCategory(nameLowerCase));
            if (!categoryOpt.isPresent()) category.save();
            recipe.getCategories().add(category);
        }
        if (recipe.getCategories().isEmpty())
            throw new BadRequestException(RequestError.BAD_FORMAT);
        for (String name : input.ingredientNames) {
            if (!StringUtils.isAlphaSpace(name))
                throw new BadRequestException(RequestError.BAD_FORMAT);
            final String nameLowerCase = name.toLowerCase();
            final Optional<Ingredient> ingredientOpt = IngredientService
                    .getInstance()
                    .getByName(nameLowerCase);
            final Ingredient ingredient = ingredientOpt
                    .orElse(new Ingredient(nameLowerCase));
            if (!ingredientOpt.isPresent()) ingredient.save();
            recipe.getIngredients().add(ingredient);
        }
        if (recipe.getIngredients().isEmpty())
            throw new BadRequestException(RequestError.BAD_FORMAT);
        final Media image = MediaService.getInstance()
                .get(input.imageId)
                .orElseThrow(() -> new BadRequestException(RequestError.BAD_FORMAT));
        recipe.setImage(image);
        // TODO Add author to recipe
        recipe.save();
        return recipe;
    }

    private String capitalize(String text) {
        final String result = text.toLowerCase();
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }
}
