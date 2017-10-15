package models.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.recipe.Recipe;
import models.recipe.RecipeBook;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "PremiumUser")
public class PremiumUser extends User {

    private LocalDate expirationDate;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Recipe> recipes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonManagedReference
    private List<RecipeBook> recipeBooks;

    public PremiumUser() {}

    public PremiumUser(String name, String lastName, String email, String profilePic) {
        super(name, lastName, email, profilePic);
    }

    public PremiumUser(String name, String lastName, String email, String profilePic, List<Recipe> recipes, List<RecipeBook> recipeBooks) {
        this(name, lastName, email, profilePic);
        this.recipes = recipes;
        this.recipeBooks = recipeBooks;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public List<RecipeBook> getRecipebooks() {
        return recipeBooks;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setRecipebooks(List<RecipeBook> recipeBooks) {
        this.recipeBooks = recipeBooks;
    }

    public LocalDate getExpirationDate() { return expirationDate; }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() { return LocalDate.now().isAfter(expirationDate); }
}
