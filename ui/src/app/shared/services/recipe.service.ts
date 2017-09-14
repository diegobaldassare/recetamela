import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http"
import {RecipeCategory} from "../models/recipe/recipe-category";
import 'rxjs/add/operator/map'
import {Ingredient} from "../models/recipe/ingredient";
import {ApiService} from "./api-service";
import {Recipe} from "../models/recipe/recipe";

@Injectable()
export class RecipeService extends ApiService {

  private URL: string = `${ApiService.API_URL}/recipe`;

  constructor(private http: HttpClient){
    super();
  }

  getRecipe(id): Promise<Recipe> {
    return this.http.get<Recipe>(`${this.URL}/${id}`).toPromise();
  }

  getRecipeCategories(): Promise<RecipeCategory[]> {
    return this.http.get<RecipeCategory[]>(`${this.URL}/categories/all`).toPromise();
  }

  getIngredients(): Promise<Ingredient[]> {
    return this.http.get<Ingredient[]>(`${this.URL}/ingredients/all`).toPromise();
  }

  createRecipe(r: Recipe): Promise<Recipe> {
    return this.http.post<Recipe>(this.URL, r).toPromise();
  }

  modifyRecipe(id: string, r: Recipe): Promise<Recipe> {
    return this.http.put<Recipe>(`${this.URL}/${id}`, r).toPromise();
  }
}
