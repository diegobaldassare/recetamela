import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
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
  followers: User[] = [];
  following: User[] = [];
  subscribed: boolean;


  ngOnInit() {
    this.route.params
      .subscribe(
      (params: Params) => {
        const id = params['id'];
        this.userService.getUser(id).then(user => {
          this.user = user;
          this.fetched = true;
          this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
          this.fetchFollowers();
          this.fetchFollowing();
        }, () => { this.fetched = true });
      }
    );

    this.recipes = [];
    const id = this.route.snapshot.params['id'];
    this.recipeService.getUserRecipes(id).then(recipes =>
      this.recipes = recipes
    );
    this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
  }

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private recipeService: RecipeService,
    private http: HttpClient,) {
  }

  subscribe() {
    this.userService.followUser(this.user.id).subscribe((res : User) => {
      this.followers.push(res);
      this.subscribed = true;
    });
  }

  unSubscribe() {
    this.userService.unFollowUser(this.user.id).subscribe((res : User) => {
      const index = this.followers.map(u => u.id).indexOf(res.id, 0);
      if (index > -1) {
        this.followers.splice(index, 1);
        this.subscribed = false;
      }
    });
  }

  fetchFollowers() {
    this.userService.getFollowers(this.route.snapshot.params['id']).subscribe((res : User[]) => {
      this.followers = res;
      this.subscribed = (this.followers.map(u => u.id).indexOf(this.loggedUser.id, 0) > -1);
    });
  }

  fetchFollowing() {
    this.userService.getFollowing(this.route.snapshot.params['id']).subscribe((res : User[]) => {
      this.following = res;
    });
  }


}
