package services.recipe;

import com.avaje.ebean.Model;
import models.recipe.Recipe;
import models.recipe.RecipeBook;
import services.Service;

import java.util.List;

public class RecipeBookService extends Service<RecipeBook> {

    private static RecipeBookService instance;

    private RecipeBookService(Model.Finder<Long, RecipeBook> finder){
        super(finder);
    }

    public static RecipeBookService getInstance(){
        if (instance == null) instance = new RecipeBookService(new Model.Finder<>(RecipeBook.class));
        return instance;
    }

    public List<RecipeBook> getAllUserRecipeBook(Long id) {
        return getFinder().where().eq("creator_id", id).findList();
    }

    public void deleteRecipe(Recipe recipe) {
        getFinder().query()
                .where()
                .in("recipes", recipe)
                .findList()
                .forEach(recipeBook -> {
                    recipeBook.getRecipes().remove(recipe);
                    recipeBook.update();
                });
    }
}
