import { Recipe } from "../../shared/models/recipe/recipe";

export abstract class RecipeFormContainer {
  public instance: RecipeFormContainer = this;
  public recipe: Recipe;
  public categories = {};
  public selectedCategories = {};
  public ingredients = {};
  public selectedIngredients = {};

  public abstract submit(): Promise<Recipe>;
}
