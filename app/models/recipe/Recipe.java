package models.recipe;

import models.BaseModel;
import models.User;
import models.Media;

import javax.persistence.*;
import java.util.List;

@Entity
public class Recipe extends BaseModel {

    @Column(nullable = false)
    private String name, description, steps;

    private String videoUrl;

    @Column(nullable = false)
    private int difficulty;

    @OneToOne(cascade = CascadeType.REMOVE)
    @Column(nullable = false)
    private Media image;

    // TODO @Column(nullable = false)
    @ManyToOne
    private User author;

    @ManyToMany
    @JoinTable(name="RECIPE_INGREDIENT", joinColumns= @JoinColumn(name="RECIPE_ID", referencedColumnName="id"), inverseJoinColumns= @JoinColumn(name="INGREDIENT_ID", referencedColumnName="id"))
    private List<Ingredient> ingredients;

    @ManyToMany
    @JoinTable(name="RECIPE_CATEGORY", joinColumns= @JoinColumn(name="RECIPE_ID", referencedColumnName="id"), inverseJoinColumns= @JoinColumn(name="CATEGORY_ID", referencedColumnName="id"))
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
}