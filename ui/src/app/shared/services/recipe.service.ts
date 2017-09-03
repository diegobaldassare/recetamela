import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Category} from "../models/recipe/category";
import 'rxjs/add/operator/map'

@Injectable()
export class RecipeService {

  private url: string = "http://localhost:9000/api/recipe/";

  constructor(private http: Http){}

  getRecipe(id): Promise<any> {
    return this.http.get(this.url + id).map(r => r.json()).toPromise();
  }

  getRecipeCategories(): Promise<Category[]> {
    return this.http.get(this.url + 'categories/all').map(r => r.json()).toPromise();
  }

  // postRecipe(recipeInput): Observable<Response> {
  //   var id = JSON.parse(localStorage.getItem('user')).id;
  //   return this.http.post(this.url + 'users' + id + 'recipe', JSON).map(this.extractData);
  // }
}
