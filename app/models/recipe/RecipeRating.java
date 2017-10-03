package models.recipe;

import models.BaseModel;
import models.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class RecipeRating extends BaseModel{

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private int rating;

    public RecipeRating() {
    }

    public RecipeRating(User user, int rating) {
        this.user = user;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
