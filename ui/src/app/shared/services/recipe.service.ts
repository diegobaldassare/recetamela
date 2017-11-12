import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http"
import {RecipeCategory} from "../models/recipe/recipe-category";
import 'rxjs/add/operator/map'
import {Ingredient} from "../models/recipe/ingredient";
import {ApiService} from "./api-service";
import {Recipe} from "../models/recipe/recipe";
import {RecipeSearchQuery} from "../../recipes/search-recipes/recipe-search-query";
import {RequestOptions, RequestOptionsArgs} from "@angular/http";
import {RecipeRating} from "../models/recipe/recipe-rating";
import {User} from "../models/user-model";
import {RecipeCommentary} from "../models/recipe/recipe-comment";
import {BooleanResponse} from "../models/boolean-response";

@Injectable()
export class RecipeService extends ApiService {

  private URL: string = `${ApiService.API_URL}/recipe`;

  constructor(private http: HttpClient){
    super();
  }

  getRecipe(id): Promise<Recipe> {
    return this.http.get<Recipe>(`${this.URL}/${id}`).toPromise();
  }

  getRecipeAuthor(id): Promise<User> {
    return this.http.get<User>(`${this.URL}/author/${id}`).toPromise();
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

  getUserRecipes(id: string) : Promise<Recipe[]> {
    return this.http.get<Recipe[]>(`/api/user/${id}/recipes`).toPromise();
  }

  addRating(id: string, rating: RecipeRating) : Promise<any> {
    return this.http.post(`${this.URL}/${id}/rate`, rating).toPromise();
  }

  addLikeByChef(id: string, recipe: Recipe) : Promise<Recipe> {
    return this.http.post<Recipe>(`${this.URL}/${id}/like`, recipe).toPromise();
  }

  deleteLikeByChef(id: string) : Promise<Recipe> {
    return this.http.delete<Recipe>(`${this.URL}/${id}/dislike`).toPromise();
  }

  getRecipeLikesByChef(id: string) : Promise<any> {
    return this.http.get(`${this.URL}/${id}/likes`).toPromise();
  }

  hasChefLikedRecipe(id: string) : Promise<BooleanResponse> {
    return this.http.get<BooleanResponse>(`${this.URL}/${id}/liked`).toPromise();
  }

  getRatingFromUser(recipeId: string) : Promise<any> {
    return this.http.get(`${this.URL}/rating/${recipeId}`).toPromise();
  }

  search(q: RecipeSearchQuery): Promise<Recipe[]> {
    const params = new HttpParams()
      .set("name", q.name)
      .set("categories", q.categories)
      .set("ingredients", q.ingredients)
      .set("difficulty", q.difficulty.toString())
      .set("author", q.author);
    return this.http.get<Recipe[]>(`${this.URL}s`, { params }).toPromise();
  }

  getComments(id: string) : Promise<RecipeCommentary[]> {
    return this.http.get<RecipeCommentary[]>(`${this.URL}/${id}/comments`).toPromise();
  }

  getTopRankingRecipes() : Promise<Recipe[]> {
    return this.http.get<Recipe[]>(`/api/recipes/topRanking`).toPromise();
  }
}
