import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {HttpClient} from "@angular/common/http"
import {RecipeCategory} from "../models/recipe/recipe-category";
import 'rxjs/add/operator/map'
import {Ingredient} from "../models/recipe/ingredient";
import {ApiService} from "./api-service";
import {RecipeInput} from "../models/recipe/recipe-input";
import {Recipe} from "../models/recipe/recipe";
import {HttpService} from "./http.service"

@Injectable()
export class RecipeService extends ApiService {

  private URL: string = `${ApiService.API_URL}/recipe`;

  constructor(private http: Http){
    super();
  }

  getRecipe(id): Promise<any> {
    return this.http.get(`${this.URL}/${id}`).map(r => r.json()).toPromise();
  }

  getRecipeCategories(): Promise<RecipeCategory[]> {
    return this.http.get(`${this.URL}/categories/all`).map(r => r.json()).toPromise();
  }

  getIngredients(): Promise<Ingredient[]> {
    return this.http.get(`${this.URL}/ingredients/all`).map(r => r.json()).toPromise();
  }

  createRecipe(input: RecipeInput): Promise<Recipe> {
    return this.http.post(this.URL, input).map(r => r.json()).toPromise();
  }

  modifyRecipe(id: string, input: RecipeInput): Promise<Recipe> {
    return this.http.put(`${this.URL}/${id}`, input).map(r => r.json()).toPromise();
  }
}
