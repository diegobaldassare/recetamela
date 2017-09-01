package models.recipe;

import models.BaseModel;
import models.media.Media;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
public class Recipe extends BaseModel {

    @Column(nullable = false)
    private String name, description, steps;

    private String videoUrl;

    @Column(nullable = false)
    private int difficulty;

    @Column(nullable = false)
    private Media image;

    // @Column(nullable = false)
    // @ManyToOne
    // private User author;

    @ManyToMany
    private Collection<Ingredient> ingredients;

    @ManyToMany
    private Collection<RecipeCategory> categories;

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