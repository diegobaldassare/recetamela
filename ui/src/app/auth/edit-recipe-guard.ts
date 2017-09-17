import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {RecipeService} from "../shared/services/recipe.service";
import {Injectable} from "@angular/core";
import {User} from "../shared/models/user-model";
import 'rxjs/add/observable/fromPromise'

@Injectable()
export class EditRecipeGuard implements CanActivate{

  constructor(private recipeService: RecipeService, private router: Router){
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    const id = route.url[1].path; //Retrieves from current URL the ID of the recipe the user is attempting to edit

    return Observable.fromPromise(this.recipeService.getRecipe(id)).map(res => {
      const u : User = JSON.parse(localStorage.getItem("user")) as User;
      if (u.id == res.author.id) {
        return true;
      } else {
        this.router.navigate(['/']);
        return false;
      }
    });
  }
}
