import {Ingredient} from "./ingredient";
import {RecipeCategory} from "./recipe-category";
import {Media} from "../media";
import {RecipeStep} from "./recipe-step";

export class Recipe {
  id: string;
  name: string;
  description: string;
  videoUrl: string;
  difficulty: string;
  duration: string;
  serves: string;
  images: Media[];
  steps: RecipeStep[];
  ingredients: Ingredient[];
  categories: RecipeCategory[];
}
