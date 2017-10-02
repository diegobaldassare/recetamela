package models.recipe;

import java.util.*;

public class RecipeSearchQuery {
    public String name, author, difficulty;
    public Set<String> categories, ingredients;

    public RecipeSearchQuery(String name, String categories, String ingredients, String difficulty, String author) {
        this.name = name;
        this.categories = categories.length() == 0 ? new HashSet<>() : new HashSet<>(Arrays.asList(categories.split(",")));
        this.ingredients = ingredients.length() == 0 ? new HashSet<>() : new HashSet<>(Arrays.asList(ingredients.split(",")));
        this.difficulty = difficulty;
        this.author = author;
    }
}
