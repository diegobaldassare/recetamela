import {Ingredient} from "./ingredient";
import {RecipeCategory} from "./recipe-category";
import {Media} from "../media";

export class Recipe {
  id; name; description; steps; videoUrl; difficulty: String;
  image: Media;
  ingredients: Ingredient[];
  categories: RecipeCategory[];
}
