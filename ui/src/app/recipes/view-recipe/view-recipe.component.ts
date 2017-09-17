import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { DomSanitizer } from '@angular/platform-browser';
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeService} from "../../shared/services/recipe.service";
import {User} from "../../shared/models/user-model";
import {SharedService} from "../../shared/services/shared.service";

@Component({
  selector: 'app-view-recipe',
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.scss']
})
export class ViewRecipeComponent implements OnInit {

  private recipe: Recipe;
  public fetched: boolean;
  private viewer: User = JSON.parse(localStorage.getItem("user"));

  constructor(
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private recipeService: RecipeService,
    private sharedService: SharedService,
    private router: Router
  ){}

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.recipeService.getRecipe(id).then(recipe => {
      this.recipe = recipe;
      this.fetched = true;
    }, () => { this.fetched = true });
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

  private get embedVideoUrl() {
    const split = this.recipe.videoUrl.split('v=');
    const url = `https://www.youtube.com/embed/${split[1]}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
}
