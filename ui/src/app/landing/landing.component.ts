import { Component, OnInit } from '@angular/core';
import {Recipe} from "../shared/models/recipe/recipe";
import {RecipeService} from "../shared/services/recipe.service";

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  topRecipes: Recipe[];

  constructor(private recipeService: RecipeService) { }

  ngOnInit() {
    this.recipeService.getTopRankingRecipes().then((res: Recipe[]) => {
      this.topRecipes = res;
    });
  }

}
