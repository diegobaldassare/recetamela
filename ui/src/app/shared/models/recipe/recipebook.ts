import {Recipe} from "./recipe";
import {User} from "../user-model";

export class RecipeBook {
  public id: string;
  public name: string;
  public recipes: Recipe[];
  public creator: User;
}
