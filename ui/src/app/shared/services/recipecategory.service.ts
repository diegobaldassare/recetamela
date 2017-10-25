import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {RecipeCategory} from "../models/recipe/recipe-category";
import {Observable} from "rxjs";

@Injectable()
export class RecipeCategoryService extends ApiService {

  private URL: string = `${ApiService.API_URL}/recipe`;

  constructor(private http: HttpClient) {
    super();
  }

  create(category: RecipeCategory) : Promise<RecipeCategory> {
    return this.http.post<RecipeCategory>(`${this.URL}/category/create`, category).toPromise();
  }

  update(categoryId: string, category: RecipeCategory) : Promise<RecipeCategory> {
    return this.http.put<RecipeCategory>(`${this.URL}/category/${categoryId}/update`, category).toPromise();
  }

  get(categoryId: string) : Promise<RecipeCategory> {
    return this.http.get<RecipeCategory>(`${this.URL}/category/${categoryId}`).toPromise();
  }

  public getUserCategories(id: string) : Observable<RecipeCategory[]> {
    return this.http.get<RecipeCategory[]>(`/api/user/categories/${id}`);
  }

  public getUnFollowedCategories(id: string) : Observable<RecipeCategory[]> {
    return this.http.get<RecipeCategory[]>(`/api/user/categories/unFollowed/${id}`);
  }

  getByName(categoryName: string) : Promise<RecipeCategory> {
    return this.http.get<RecipeCategory>(`${this.URL}/category/name/${categoryName}`).toPromise();
  }

  getAll() : Promise<RecipeCategory[]> {
    return this.http.get<RecipeCategory[]>(`${this.URL}/categories/all`).toPromise();
  }

  delete(categoryId: string) : Promise<any> {
    return this.http.delete<any>(`${this.URL}/category/${categoryId}/delete`).toPromise();
  }

  searchCategories(query: string) : Promise<any[]> {
    return this.http.get<any[]>(`${this.URL}/categories/${query}`).toPromise();
  }

  subscribeToCategory(id: string) {
    return this.http.post<RecipeCategory>(`/api/user/categories/subscribe/${id}`, "").toPromise();
  }

  unSubscribeToCategory(id: string) {
    return this.http.post<RecipeCategory>(`/api/user/categories/unSubscribe/${id}`, "").toPromise();
  }
}
