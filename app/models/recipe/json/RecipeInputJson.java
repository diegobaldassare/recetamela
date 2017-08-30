package models.recipe.json;

public class RecipeInputJson {
    public String name, description, steps, videoUrl;
    public int difficulty;
    public long imageId;
    public long[] categoryIds;
    public String[] ingredientNames;
}
