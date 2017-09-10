package models.recipe;

import models.BaseModel;
import models.User;
import models.Media;

import javax.persistence.*;
import java.util.List;

@Entity
public class Recipe extends BaseModel {

    @Column(nullable = false)
    private String name, description;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<RecipeStep> recipeSteps;

    private String videoUrl;

    @Column(nullable = false)
    private int difficulty;

    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Media> images;

    // TODO @Column(nullable = false)
    @ManyToOne
    private User author;

    @ManyToMany
    private List<Ingredient> ingredients;

    @ManyToMany
    private List<RecipeCategory> categories;

    public Recipe() {}

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

    public List<Media> getImages() {
        return images;
    }

    public void setImages(List<Media> images) {
        this.images = images;
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<RecipeCategory> categories) {
        this.categories = categories;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }
}