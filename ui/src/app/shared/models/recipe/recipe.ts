import {Ingredient} from "./ingredient";
import {RecipeCategory} from "./recipe-category";
import {Media} from "../media";
import {RecipeStep} from "./recipe-step";
import {User} from "../user-model";

export class Recipe {
  id: string = "";
  name: string = "";
  description: string = "";
  videoUrl: string = "";
  difficulty: string = "3";
  images: Media[] = [];
  steps: RecipeStep[] = [];
  ingredients: Ingredient[] = [];
  categories: RecipeCategory[] = [];
  servings: number = 1;
  duration: number = 20;
  author: User;
}
