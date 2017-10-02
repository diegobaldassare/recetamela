import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeCategory} from "../../shared/models/recipe/recipe-category";
import {Ingredient} from "../../shared/models/recipe/ingredient";
import {IngredientFormula} from "../../shared/models/recipe/ingredient-formula";

export abstract class RecipeFormContainer {
  public instance: RecipeFormContainer = this;
  public recipe: Recipe;
  public categories = {};
  public selectedCategories = {};
  public ingredients = {};
  public selectedIngredients = {};

  public abstract submit(): Promise<Recipe>;
}
