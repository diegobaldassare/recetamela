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
            int size = recipe.getRatings().size();
            recipe.setRating((recipe.getRating()*size - rating.getRating() + recipeRating.getRating()) / (size));
            rating.setRating(recipeRating.getRating());
            rating.save();
        } else {
        avRating(recipe, recipeRating.getRating());
        recipe.getRatings().add(recipeRating);
        }
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

    private void avRating(Recipe recipe, int rating) {
        if(recipe.getRating() == 0) recipe.setRating(rating);
        recipe.setRating((recipe.getRating()*recipe.getRatings().size() + rating) / (recipe.getRatings().size()+1));
    }

    public RecipeRating getRatingByUser(long userId, Recipe recipe) {
        for (RecipeRating r: recipe.getRatings()) {
            if(r.getUser().getId() == userId) return r;
        }
        return new RecipeRating();
    }

//    public void updateRating(Recipe recipe, RecipeRating newRecipeRating, RecipeRating oldRecipeRating) throws BadRequestException{
//        checkRating(newRecipeRating);
//        int size = recipe.getRatings().size();
//        recipe.setRating((recipe.getRating()*size - oldRecipeRating.getRating() + newRecipeRating.getRating()) / (size));
//        oldRecipeRating.setRating(newRecipeRating.getRating());
//        oldRecipeRating.save();
//        recipe.save();
//    }
}
