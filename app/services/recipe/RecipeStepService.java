package services.recipe;

import com.avaje.ebean.Model;
import models.recipe.RecipeStep;
import services.MediaService;
import services.Service;

public class RecipeStepService extends Service<RecipeStep>{

    private static RecipeStepService instance;

    private RecipeStepService(Model.Finder<Long, RecipeStep> finder) {
        super(finder);
    }

    public static RecipeStepService getInstance() {
        if(instance == null) instance = new RecipeStepService(new Model.Finder<>(RecipeStep.class));
        return instance;
    }

    //The format of the description is checked before.
    public RecipeStep save(String description, long idImage){
        final RecipeStep recipeStep = new RecipeStep();
        recipeStep.setDescription(description);
        MediaService.getInstance().get(idImage).ifPresent(recipeStep::setImage);
        return recipeStep;
    }
}
