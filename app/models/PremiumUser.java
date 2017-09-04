package models;


import models.recipe.Recipe;

import javax.persistence.OneToMany;
import java.util.List;

public class PremiumUser extends User {

    public PremiumUser() {}

    public PremiumUser(String name, String lastName, String email, Media profilePic, List<Recipe> recipes) {
        super(name, lastName, email, profilePic);
        this.recipes = recipes;
    }

    @OneToMany
    private List<Recipe> recipes;

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
