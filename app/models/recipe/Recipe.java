package models.recipe;

import models.BaseModel;

public class Recipe extends BaseModel<Recipe> {
    public String name, description, steps;
    public int difficulty;
}