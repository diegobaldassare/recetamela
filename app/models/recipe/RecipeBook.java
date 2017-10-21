package models.recipe;


import com.fasterxml.jackson.annotation.*;
import models.BaseModel;
import models.user.PremiumUser;
import models.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class RecipeBook extends BaseModel {

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"author", "comments"})
    private List<Recipe> recipes;

    @ManyToOne(optional = false)
    @JsonBackReference
    private User creator;

    public RecipeBook() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(PremiumUser creator) {
        this.creator = creator;
    }
}
