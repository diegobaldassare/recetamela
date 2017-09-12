package models;


import models.recipe.Recipe;
import models.recipe.RecipeBook;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue(value = "PremiumUser")
public class PremiumUser extends User {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Recipe> recipes;
    @ManyToMany
    private Set<RecipeBook> recipeBooks;

    public PremiumUser() {}

    public PremiumUser(String name, String lastName, String email, String profilePic) {
        super(name, lastName, email, profilePic);
    }

    public PremiumUser(String name, String lastName, String email, String profilePic, List<Recipe> recipes, Set<RecipeBook> recipeBooks) {
        this(name, lastName, email, profilePic);
        this.recipes = recipes;
        this.recipeBooks = recipeBooks;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Set<RecipeBook> getRecipebooks() {
        return recipeBooks;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setRecipebooks(Set<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }
}
