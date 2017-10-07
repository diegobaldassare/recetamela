package models.recipe;

import com.avaje.ebean.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import models.BaseModel;
import models.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class RecipeCategory extends BaseModel {

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "followedCategories")
    @JsonBackReference
    private List<User> followers;

    public RecipeCategory() {}

    public RecipeCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
}
