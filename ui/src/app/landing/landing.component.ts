import { Component, OnInit } from '@angular/core';
import {RecipeService} from "../shared/services/recipe.service";
import {Recipe} from "../shared/models/recipe/recipe";

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  // private recipes: Recipe[] = [];

  constructor(
    // private recipeService: RecipeService
  ) { }

  ngOnInit() {
    // for(let i = 1; i < 4; i++) {
    //   this.recipeService.getRecipe(i).then(recipe => this.recipes[i] = recipe);
    // }
  }
}
