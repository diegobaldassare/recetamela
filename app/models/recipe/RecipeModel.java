package models.recipe;

import models.BaseModel;

public class RecipeModel extends BaseModel<RecipeModel> {
    public String name, description, steps;
    public int difficulty;
}