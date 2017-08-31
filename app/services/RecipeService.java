package services;

import com.avaje.ebean.Model;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.json.RecipeInputJson;
import server.error.RequestError;

import java.util.List;

/**
 * ABM for recipe.
 */

public class RecipeService extends Service<Recipe>{

    private static RecipeService instance;

    protected RecipeService(Model.Finder<Long, Recipe> finder){
        super(finder);
    }

    public static RecipeService getInstance(){
        if(instance == null) instance = new RecipeService(new Model.Finder<>(Recipe.class));
        return instance;
    }

    /**
     * Persist recipe into database.
     * Receive RecipeInputJson ("RIJ"), check if name is null and if any of
     * the categories id's doesn't exist in db. Create Recipe object with all
     * the information provided by the RIJ and save it to the db.
     */
    public Recipe saveRecipe(RecipeInputJson recipeInputJson){
        Recipe recipe = new Recipe();
        if(recipeInputJson.name == null) throw new IllegalArgumentException(RequestError.BAD_REQUEST.toString());
        recipe.setName(recipeInputJson.name);
        recipe.setDescription(recipeInputJson.description);
        recipe.setSteps(recipeInputJson.steps);
        Model.Finder<Long, RecipeCategory> findCategory = new Model.Finder<>(RecipeCategory.class);
        for (Long id: recipeInputJson.categoryIds) {
            recipe.getCategories().add(findCategory.byId(id));
        }
//        recipe.setImage();
//        recipe.setIngredients(recipeInputJson.);
//        ebeanServer.save(recipe);
        return recipe;
    }

    /**
     * Get recipe by id.
     */
    public Recipe getRecipe(Long id){
        return finder.byId(id);
    }

    /**
     * Get all recipes in db.
     * TODO: To return as Json all the recipes, in the controller return "ok(toJson(getAllRecipes()))"
     */
    public List<Recipe> getAllRecipes(){
        return finder.all();
    }

    /**
     * Get list of recipes in the specified category.
     */
    public List<Recipe> getRecipesByCategory(RecipeCategory recipeCategory){
        return finder.where().eq("categories", recipeCategory).findList();
    }

    /**
     * Get list of recipes by ingredients.
     */
    public List<Recipe> getRecipesByIngredients(Ingredient ingredient){
        return finder.where().eq("ingredients", ingredient).findList();
    }
}
