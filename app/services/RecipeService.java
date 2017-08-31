package services;

import com.avaje.ebean.Model.Finder;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.json.RecipeInputJson;
import server.error.RequestError;
import server.exception.BadRequestException;

import java.util.Collection;
import java.util.List;

public class RecipeService extends Service<Recipe> {

    private static RecipeService instance;

    protected RecipeService(Finder<Long, Recipe> finder){
        super(finder);
    }

    public static RecipeService getInstance(){
        if (instance == null) instance = new RecipeService(new Finder<>(Recipe.class));
        return instance;
    }

    /**
     * Persist recipe into database.
     * Receive RecipeInputJson ("RIJ"), check if name is null and if any of
     * the categories id's doesn't exist in db. Create Recipe object with all
     * the information provided by the RIJ and save it to the db.
     */
    public Recipe save(RecipeInputJson recipeInputJson) throws BadRequestException {
        Recipe recipe = new Recipe();
        if(recipeInputJson.name == null) throw new BadRequestException(RequestError.BAD_REQUEST);
        recipe.setName(recipeInputJson.name);
        recipe.setDescription(recipeInputJson.description);
        recipe.setSteps(recipeInputJson.steps);
        Finder<Long, RecipeCategory> findCategory = new Finder<>(RecipeCategory.class);
        for (Long id: recipeInputJson.categoryIds) {
            recipe.getCategories().add(findCategory.byId(id));
        }
        /*recipe.setImage();
        recipe.setIngredients(recipeInputJson.);
        recipe.save();*/
        return recipe;
    }

    /**
     * Get recipe by id.
     */
    public Recipe get(Long id){
        return finder.byId(id);
    }

    /**
     * Get all recipes in db.
     * TODO: To return as Json all the recipes, in the controller return "ok(toJson(getAllRecipes()))"
     */
    public List<Recipe> getMany(int limit){
        return finder.all();
    }

    /**
     * Get list of recipes in the specified category.
     */
    public List<Recipe> getByCategory(RecipeCategory category){
        return finder.where().eq("categories", category).findList();
    }

    /**
     * Get list of recipes by ingredients.
     */
    public List<Recipe> getByIngredients(Collection<Ingredient> ingredients){
        return finder.where().eq("ingredients", ingredients).findList(); // should be .contains()
    }
}
