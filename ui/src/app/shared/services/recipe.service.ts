import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Http, Response} from "@angular/http";
import {Recipe} from "../models/recipe/recipe";
import {Category} from "../models/recipe/category";
import 'rxjs/add/operator/map'

@Injectable()
export class RecipeService {

  private url: string = "http://localhost:9000/api/recipe/";

  constructor(private http: Http){}

  getRecipe(recipeid): Observable<Response> {
    return this.http.get(this.url + recipeid).map(this.extractData);
  }

  private extractData(res:Response) {
    let body = res.json();
    return body || [];
  }

  getRecipeCategories(): Observable<Category[]> {
    return this.http.get(this.url + 'categories/all').map(this.extractData);
  }

  // postRecipe(recipeInput): Observable<Response> {
  //   var id = JSON.parse(localStorage.getItem('user')).id;
  //   return this.http.post(this.url + 'users' + id + 'recipe', JSON).map(this.extractData);
  // }
}
