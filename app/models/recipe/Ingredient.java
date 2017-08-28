package models.recipe;

import models.BaseModel;

public class Ingredient extends BaseModel<Ingredient>{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
