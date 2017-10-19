import {Injectable} from "@angular/core";
import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";
import {RecipeCommentary} from "../models/recipe/recipe-comment";

@Injectable()
export class RecipeCommentaryService extends ApiService {

  private URL: string = `${ApiService.API_URL}/recipe/comment`;

  constructor(private http: HttpClient) {
    super();
  }

  createRecipeCommentary(comment: RecipeCommentary, recipeId: string) : Promise<RecipeCommentary> {
    return this.http.post<RecipeCommentary>(`${this.URL}/create/${recipeId}`, comment).toPromise();
  }

  updateRecipeCommentary(comment: RecipeCommentary, commentId: string) : Promise<RecipeCommentary> {
    return this.http.put<RecipeCommentary>(`${this.URL}/${commentId}/update`, comment).toPromise();
  }

  getRecipeCommentary(commentId: string) : Promise<RecipeCommentary> {
    return this.http.get<RecipeCommentary>(`${this.URL}/${commentId}`).toPromise();
  }

  deleteRecipeCommentary(commentId: string) : Promise<any> {
    return this.http.delete<any>(`${this.URL}/${commentId}/delete`).toPromise();
  }
}
