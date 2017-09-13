import {Recipe} from "../shared/models/recipe/recipe";
import {RecipeCategory} from "../shared/models/recipe/recipe-category";
import {Ingredient} from "../shared/models/recipe/ingredient";

export abstract class RecipeFormContainer {
  public instance: RecipeFormContainer = this;
  public recipe: Recipe = new Recipe();
  public categories: RecipeCategory[] = [];
  public selectedCategories: RecipeCategory[] = [];
  public ingredients: Ingredient[] = [];
  public selectedIngredients: Ingredient[] = [];

  public abstract submit(): Promise<Recipe>;
}
