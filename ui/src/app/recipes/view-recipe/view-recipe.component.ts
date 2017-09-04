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
  private fetched: boolean;

  constructor(
    private route: ActivatedRoute,
    public sanitizer: DomSanitizer,
    private _recipeService: RecipeService
  ){}

  ngOnInit() {
    const id = +this.route.snapshot.params['id'];
    this._recipeService.getRecipe(id).then(recipe => {
      this.recipe = recipe;
      this.fetched = true;
    }, () => { this.fetched = true });
  }
}
