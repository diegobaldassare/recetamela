package services.recipe;

import com.avaje.ebean.Model.Finder;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.Service;

public class RecipeCategoryService extends Service<RecipeCategory> {
    private static RecipeCategoryService instance;

    private RecipeCategoryService(Finder<Long, RecipeCategory> finder) {
        super(finder);
    }

    public static RecipeCategoryService getInstance() {
        if (instance == null)
            instance = new RecipeCategoryService(new Finder<>(RecipeCategory.class));
        return instance;
    }

    /**
     * Persists recipeCategory into database. Receives the name of the category from the request.
     * @param name String name from the request.
     * @return A persisted RecipeCategory model instance.
     * @throws BadRequestException If the request input is invalid.
     */
    public RecipeCategory save(String name) throws BadRequestException {
        if(getByName(name) != null) throw new BadRequestException(RequestError.CATEGORY_EXISTS);
        RecipeCategory recipeCategory = new RecipeCategory(name);
        recipeCategory.save();
        return recipeCategory;
    }

    /**
     * Update recipeCategory from database. Receives the new name of the category from the request.
     * @param newName String newName to update from the request.
     * @return An updated RecipeCategory model instance.
     * @throws BadRequestException If the category doesn't exist.
     */
    public RecipeCategory update(String newName, long categoryId) throws BadRequestException {
        if(getById(categoryId) == null) throw new BadRequestException(RequestError.CATEGORY_NOT_EXISTS);
        RecipeCategory recipeCategory = getById(categoryId);
        recipeCategory.setName(newName);
        recipeCategory.update();
        return recipeCategory;
    }

    /**
     * Delete recipeCategory from database. Receives the name of the category from the request.
     * @param name String name to delete from the request.
     * @throws BadRequestException If the category doesn't exist.
     */
    public void delete(String name) throws BadRequestException {
        if(getByName(name) == null) throw new BadRequestException(RequestError.CATEGORY_NOT_EXISTS);
        RecipeCategory recipeCategory = getByName(name);
        recipeCategory.delete();
    }

    /**
     * Get category by name.
     * @param name Name of the category.
     * @return RecipeCategory if found. If there's more than one category with name an exception
     *         and null if category was not found.
     */
    public RecipeCategory getByName(String name){
        return finder.where().eq("name", name).findUnique();
    }

    /**
     * Get category by id.
     * @param id Id of the category.
     * @return RecipeCategory with the id provided or null if not found.
     */
    public RecipeCategory getById(long id) {
        return finder.where().eq("id", id).findUnique();
    }
}
