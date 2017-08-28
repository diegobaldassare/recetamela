package models.recipe;

import models.BaseModel;

import javax.persistence.Entity;

@Entity
public class Ingredient extends BaseModel<Ingredient>{

    private String name;

    public Ingredient() { }

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
