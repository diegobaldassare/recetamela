import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {RecipeCategory} from "../models/recipe/recipe-category";
import 'rxjs/add/operator/map'
import {Ingredient} from "../models/recipe/ingredient";

@Injectable()
export class RecipeService {

  private url: string = "http://localhost:9000/api/recipe/";

  constructor(private http: Http){}

  getRecipe(id): Promise<any> {
    return this.http.get(this.url + id).map(r => r.json()).toPromise();
  }

  getRecipeCategories(): Promise<RecipeCategory[]> {
    return this.http.get(this.url + 'categories/all').map(r => r.json()).toPromise();
  }

  getIngredients(): Promise<Ingredient[]> {
    return this.http.get(this.url + 'ingredients/all').map(r => r.json()).toPromise();
  }
}
