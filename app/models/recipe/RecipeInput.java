package models.recipe;

public class RecipeInput {
    public String name, description, videoUrl;
    public int difficulty;
    public long[] imageIds;
    public String[] steps;
    public long[] stepsImagesIds;
    public String[] categoryNames;
    public String[] ingredientNames;
}
