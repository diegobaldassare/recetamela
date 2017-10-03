import {RecipeCategory} from "./recipe-category";
import {Media} from "../media";
import {RecipeStep} from "./recipe-step";
import {User} from "../user-model";
import {RecipeRating} from "./recipe-rating";
import {IngredientFormula} from "./ingredient-formula";

export class Recipe {
  id: string = "";
  name: string = "";
  description: string = "";
  videoUrl: string = "";
  difficulty: string = "3";
  images: Media[] = [];
  steps: RecipeStep[] = [];
  ingredients: IngredientFormula[] = [];
  categories: RecipeCategory[] = [];
  servings: number = 1;
  duration: number = 20;
  author: User;
  rating: number = 0;
  ratings: RecipeRating[] = [];
}
