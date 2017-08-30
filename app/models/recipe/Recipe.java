package models.recipe;

import models.BaseModel;
import models.media.Media;

import javax.persistence.Entity;
import java.util.Collection;

@Entity
public class Recipe extends BaseModel {
    private String name, description, steps, videoUrl;
    private int difficulty;
    private Media image;
    private Collection<Ingredient> ingredients;
    private Collection<RecipeCategory> categories;

    public Recipe() {}

    public Recipe(String name, String description, String steps, String videoUrl, int difficulty, Media image, Collection<Ingredient> ingredients, Collection<RecipeCategory> categories) {
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.image = image;
        this.image = image;
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

    public Media getImage() {
        return image;
    }

    public void setImage(Media image) {
        this.image = image;
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