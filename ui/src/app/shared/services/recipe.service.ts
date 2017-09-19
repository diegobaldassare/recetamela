import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http"
import {RecipeCategory} from "../models/recipe/recipe-category";
import 'rxjs/add/operator/map'
import {Ingredient} from "../models/recipe/ingredient";
import {ApiService} from "./api-service";
import {Recipe} from "../models/recipe/recipe";
import {RecipeSearchQuery} from "../../recipes/search-recipes/recipe-search-query";
import {RequestOptions, RequestOptionsArgs} from "@angular/http";

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

  deleteRecipe(id: string) : Promise<any> {
    return this.http.delete(`${this.URL}/${id}`).toPromise();
  }

  search(q: RecipeSearchQuery): Promise<Recipe[]> {
    const params = new HttpParams()
      .set("name", q.name)
      .set("categoryNames", q.categoryNames)
      .set("ingredientNames", q.ingredientNames)
      .set("difficulty", q.difficulty.toString())
      .set("authorName", q.authorName);
    return this.http.get<Recipe[]>(`${this.URL}s`, { params }).toPromise();
  }
}
