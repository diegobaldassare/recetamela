package services.recipe;

import com.avaje.ebean.Model;
import models.recipe.RecipeBook;
import services.Service;

public class RecipeBookService extends Service<RecipeBook> {

    private static RecipeBookService instance;

    private RecipeBookService(Model.Finder<Long, RecipeBook> finder){
        super(finder);
    }

    public static RecipeBookService getInstance(){
        if (instance == null) instance = new RecipeBookService(new Model.Finder<>(RecipeBook.class));
        return instance;
    }
}
