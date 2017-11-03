package models.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.BaseModel;
import models.Comment;
import models.Media;
import models.user.ChefUser;
import models.user.PremiumUser;
import models.user.User;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;

@Entity
public class Recipe extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 4096)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RecipeStep> steps;

    private String videoUrl;

    @Column(nullable = false)
    private int difficulty;

    @ManyToMany(cascade = REMOVE)
    private List<Media> images;

    @ManyToOne(optional = false)
    private User author;

    @OneToMany(cascade = ALL)
    private List<IngredientFormula> ingredients;

    @ManyToMany(cascade = REMOVE)
    private List<RecipeCategory> categories;

    @Column(nullable = false)
    private int servings, duration;

    private double rating;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RecipeRating> ratings;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany(cascade = REMOVE)
    @JsonIgnore
    private List<User> likesByChef;

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

    public List<IngredientFormula> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientFormula> ingredients) {
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

    public void setAuthor(PremiumUser author) {
        this.author = author;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<RecipeRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<RecipeRating> ratings) {
        this.ratings = ratings;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<User> getLikesByChef() {
        return likesByChef;
    }

    public void setLikesByChef(List<User> likesByChef) {
        this.likesByChef = likesByChef;
    }
}