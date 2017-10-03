package services.recipe;

import com.avaje.ebean.Model;
import models.recipe.Recipe;
import models.recipe.RecipeRating;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.Service;

import java.util.Objects;

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
        checkRating(recipeRating);
        RecipeRating rating = checkUserRate(recipe, recipeRating);
        if(rating != null){
            rating.setRating(recipeRating.getRating());
            rating.save();
        } else {
            recipe.getRatings().add(recipeRating);
        }
        averageRating(recipe);
        recipe.save();
    }

    private RecipeRating checkUserRate(Recipe recipe, RecipeRating recipeRating) {
        for (int i = 0; i < recipe.getRatings().size(); i++) {
            if (Objects.equals(recipeRating.getUser().getId(), recipe.getRatings().get(i).getUser().getId())) {
                return recipe.getRatings().get(i);
            }
        }
        return null;
    }

    private void checkRating(RecipeRating recipeRating) throws BadRequestException {
        if(recipeRating.getRating() < 1 || recipeRating.getRating() > 5)
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private void averageRating(Recipe recipe) {
        double rating = 0;
        for(RecipeRating rating1: recipe.getRatings()){
            rating += rating1.getRating();
        }
        recipe.setRating(Math.floor(rating/recipe.getRatings().size() * 10) / 10);
    }
}
