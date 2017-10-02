import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {RecipeService} from "../shared/services/recipe.service";
import {Recipe} from "../shared/models/recipe/recipe";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private user: User;
  public recipes: Recipe[];
  private loggedUser: User;
  public fetched: boolean;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private recipeService: RecipeService,
    private http: HttpClient,
  ) {
  }

  ngOnInit() {
    this.recipes = [];
    const id = this.route.snapshot.params['id'];
    this.userService.getUser(id).then(user => {
      this.user = user;
      this.fetched = true;
    }, () => { this.fetched = true });
    this.recipeService.getUserRecipes(id).then(recipes =>
      this.recipes = recipes
    );
    this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
  }

  sub() {
    this.http.post(`/api/user/subscribe/${this.user.id}`, "").subscribe(e => {
      // Do something with response
    });
  }


}
