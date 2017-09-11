package models.recipe;


import models.BaseModel;

import javax.persistence.*;
import java.util.List;

@Entity
public class RecipeBook extends BaseModel {

    @Column(nullable = false)
    private String name, description;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Recipe> recipes;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}