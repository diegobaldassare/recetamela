package models;

import models.user.ChefUser;
import models.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class News extends BaseModel {

    private String title;

    private String description;

    private Media image;

    private String videoURL;

    @ManyToOne
    private User author;

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

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(ChefUser author) {
        this.author = author;
    }
}
