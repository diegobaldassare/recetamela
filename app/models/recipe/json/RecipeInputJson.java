package models.recipe.json;

public class RecipeInputJson {
    private String name, description, steps, videoUrl;
    private int difficulty;
    private long imageId;
    private long[] categoryIds;
    private String[] ingredientNames;
}
