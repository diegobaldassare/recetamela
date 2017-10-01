package models.recipe;


import com.fasterxml.jackson.annotation.JsonBackReference;
import models.BaseModel;
import models.user.PremiumUser;
import models.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class RecipeBook extends BaseModel {

    @Column(nullable = false)
    private String name;

    @ManyToMany
    private List<Recipe> recipes;

    @ManyToOne(optional = false)
    @JsonBackReference
    private User creator;

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
