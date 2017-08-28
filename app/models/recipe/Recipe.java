package models.recipe;

import models.BaseModel;

import javax.persistence.Entity;
import java.util.Collection;

@Entity
public class Recipe extends BaseModel<Recipe> {
    private String name, description, steps, imageUrl, videoUrl;
    private int difficulty;
    private Collection<Ingredient> ingredients;
    private Collection<RecipeCategory> categories;

    public Recipe() {}

    public Recipe(String name, String description, String steps, String imageUrl, String videoUrl, int difficulty, Collection<Ingredient> ingredients, Collection<RecipeCategory> categories) {
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Collection<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Collection<RecipeCategory> getCategories() {
        return categories;
    }

    public void setCategories(Collection<RecipeCategory> categories) {
        this.categories = categories;
    }
}