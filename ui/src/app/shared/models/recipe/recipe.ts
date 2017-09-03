import {Ingredient} from "./ingredient";
import {Category} from "./category";
import {Media} from "../media";

export class Recipe {
  id; name; description; steps; videoUrl; difficulty: String;
  image: Media;
  ingredients: Ingredient[];
  categories: Category[];
}
