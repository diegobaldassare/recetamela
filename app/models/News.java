package models;

import models.recipe.Recipe;
import models.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class News extends BaseModel {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10000)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Media image;

    private String videoUrl;

    @ManyToOne
    @Column(nullable = false)
    private User author;

    @Column(nullable = false)
    private Date created;

    @OneToOne(cascade = CascadeType.ALL)
    private Recipe recipe;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
