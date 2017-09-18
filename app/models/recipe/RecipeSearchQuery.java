package models.recipe;

import java.util.Arrays;
import java.util.List;

public class RecipeSearchQuery {
    public String name, authorName, difficulty;
    public List<String> categoryNames, ingredientNames;

    public RecipeSearchQuery(String name, String authorName, String difficulty, String categoryNames, String ingredientNames) {
        this.name = name.toLowerCase().trim();
        this.authorName = authorName.toLowerCase().trim();
        this.difficulty = difficulty;
        this.categoryNames = Arrays.asList(categoryNames.toLowerCase().trim().split(","));
        this.ingredientNames = Arrays.asList(ingredientNames.toLowerCase().trim().split(","));
    }
}
