import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {DomSanitizer} from '@angular/platform-browser';
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeService} from "../../shared/services/recipe.service";
import {User} from "../../shared/models/user-model";
import {SharedService} from "../../shared/services/shared.service";
import {FormatService} from "../../shared/services/format.service";
import {OnClickEvent} from "angular-star-rating";
import {RecipeRating} from "../../shared/models/recipe/recipe-rating";

@Component({
  selector: 'app-view-recipe',
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.scss']
})
export class ViewRecipeComponent implements OnInit {

  recipe: Recipe;
  public fetched: boolean;
  private viewer: User = JSON.parse(localStorage.getItem("user"));
  private recipeRating: RecipeRating = new RecipeRating();
  private chefLiked: boolean;

  constructor(
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private recipeService: RecipeService,
    private sharedService: SharedService,
    private router: Router,
    private formatter: FormatService
  ){}

  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          const id = this.route.snapshot.params['id'];
          this.recipeService.getRecipe(id).then(recipe => {
            this.recipe = recipe;
            this.fetched = true;
            this.hasChefLiked();
          }, () => { this.fetched = true });
          this.recipeService.getRatingFromUser(id).then( recipeRating => {
              this.recipeRating = recipeRating;
            }
          )
        }
      );
  }

  private hasChefLiked() {
    for(let i = 0; i < this.recipe.likesByChef.length; i++) {
      if(this.recipe.likesByChef[i].id == this.viewer.id) {
        this.chefLiked = true;
      }
    }
  }

  public get editButton(): boolean {
    return this.viewer.id == this.recipe.author.id;
  }

  private checkPremium() {
    const u : User = JSON.parse(localStorage.getItem("user")) as User;
    if (u.type == 'FreeUser') {
      this.sharedService.notifyOther({upgradeForm:true});
    }
    else {
      const id = this.route.snapshot.params['id'];
      this.router.navigate(['/recetas/' + id + '/editar']);
    }
  }

  public get canAddToRecipeBook(): boolean{
    const u : User = JSON.parse(localStorage.getItem("user")) as User;
    return (u.type != 'FreeUser');
  }

  private get embedVideoUrl() {
    const split = this.recipe.videoUrl.split('v=');
    const url = `https://www.youtube.com/embed/${split[1]}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  private addRating($event:OnClickEvent) {
    const rating = new RecipeRating();
    rating.rating = $event.rating;
    this.recipeService.addRating(this.recipe.id, rating).then(recipe =>
      this.recipe = recipe
    )
  }

  private get chefCanRate(): boolean {
      return (this.viewer.type == 'ChefUser' && this.viewer.id != this.recipe.author.id);
  }

  private likeRecipe() {
    this.recipeService.addLikeByChef(this.recipe.id, this.recipe).then(recipe => {
        this.recipe = recipe;
        this.chefLiked = true;
      }
    )
  }

  private dislikeRecipe() {
      this.recipeService.deleteLikeByChef(this.recipe.id).then(recipe => {
        this.recipe = recipe;
        this.chefLiked = false;
      })
  }
}
