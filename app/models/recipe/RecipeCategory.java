package models.recipe;

import models.BaseModel;

public class RecipeCategory extends BaseModel<RecipeCategory>{

    private String name;

    public RecipeCategory() { }

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
