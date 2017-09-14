import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import { DomSanitizer } from '@angular/platform-browser';
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeService} from "../../shared/services/recipe.service";

@Component({
  selector: 'app-view-recipe',
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.scss']
})
export class ViewRecipeComponent implements OnInit {

  private recipe: Recipe;
  public fetched: boolean;

  constructor(
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private recipeService: RecipeService
  ){}

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.recipeService.getRecipe(id).then(recipe => {
      this.recipe = recipe;
      this.fetched = true;
    }, () => { this.fetched = true });
  }

  private get embedVideoUrl() {
    const split = this.recipe.videoUrl.split('v=');
    const url = `https://www.youtube.com/embed/${split[1]}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
}
