import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css']
})
export class NewRecipeComponent implements OnInit {

  private recipeInput: RecipeInput;

  constructor(public sanitizer: DomSanitizer) {
    this.recipeInput = new RecipeInput();
  }

  ngOnInit() {
  }

  saveRecipe(){}
}
