import {Injectable} from "@angular/core";
import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";
import {RecipeBook} from "../models/recipe/recipebook";

@Injectable()
export class RecipeBookService extends ApiService {

  private URL: string = `${ApiService.API_URL}/recipebook`;

  constructor(private http: HttpClient) {
    super();
  }

  create(recipeBook: RecipeBook) : Promise<RecipeBook> {
    return this.http.post<RecipeBook>(`${this.URL}/create`, recipeBook).toPromise();
  }

  get(recipeBookId: string) : Promise<RecipeBook> {
    return this.http.get<RecipeBook>(`${this.URL}/${recipeBookId}`).toPromise();
  }

  getAll() : Promise<RecipeBook[]> {
    return this.http.get<RecipeBook[]>(`${this.URL}s`).toPromise();
  }

  getUserRecipeBooks() : Promise<RecipeBook[]> {
    return this.http.get<RecipeBook[]>(`${this.URL}/all/`).toPromise();
  }

  update(recipeBookId: string) : Promise<RecipeBook> {
    return this.http.put<RecipeBook>(`${this.URL}/${recipeBookId}/update`, {}).toPromise();
  }

  delete(recipeBookId: string) : Promise<any> {
    return this.http.delete<any>(`${this.URL}/${recipeBookId}/delete`).toPromise();
  }
}
