package models.recipe;

import models.BaseModel;

import javax.persistence.Entity;

@Entity
public class RecipeCategory extends BaseModel {

    private String name;

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
}
