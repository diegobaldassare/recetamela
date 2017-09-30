package services.recipe;

import com.avaje.ebean.Model;
import models.recipe.Recipe;
import models.recipe.RecipeRating;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.Service;

public class RecipeRatingService extends Service<RecipeRating>{

    private static RecipeRatingService instance;

    private RecipeRatingService(Model.Finder<Long, RecipeRating> finder) {
        super(finder);
    }

    public static RecipeRatingService getInstance(){
        if(instance == null) instance = new RecipeRatingService(new Model.Finder<>(RecipeRating.class));
        return instance;
    }

    public void addRating(Recipe recipe, RecipeRating recipeRating) throws BadRequestException {
        if(recipeRating.getRating() < 1 || recipeRating.getRating() > 5)
            throw new BadRequestException(RequestError.BAD_FORMAT);
        recipe.getRatings().add(recipeRating);
        averageRating(recipe);
        recipe.save();
    }

    private void averageRating(Recipe recipe) {
        double rating = 0;
        for(RecipeRating rating1: recipe.getRatings()){
            rating += rating1.getRating();
        }
        recipe.setRating(rating/recipe.getRatings().size());
    }
}
