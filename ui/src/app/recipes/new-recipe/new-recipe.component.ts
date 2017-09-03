import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css']
})
export class NewRecipeComponent implements OnInit {

  private recipeInput: RecipeInput;

  constructor() {
    this.recipeInput = new RecipeInput();
  }

  ngOnInit() {
  }

  saveRecipe(){}
}
