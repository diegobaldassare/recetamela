package models.recipe;

import java.util.List;

public class RecipeInput {
    public String name, description, videoUrl;
    public int difficulty;
    public long[] imageIds;
    public List<RecipeStep> steps;
    public String[] categoryNames;
    public String[] ingredientNames;
}
