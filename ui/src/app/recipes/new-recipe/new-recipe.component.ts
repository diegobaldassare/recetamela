import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {Category} from "../../shared/models/recipe/category";
import {RecipeService} from "../../shared/services/recipe.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css']
})
export class NewRecipeComponent implements OnInit {

  private recipeInput: RecipeInput;
  private categories: Category[];

  constructor(private _recipeService: RecipeService, public sanitizer: DomSanitizer) {
    this.recipeInput = new RecipeInput();
  }

  ngOnInit() {
    this._recipeService.getRecipeCategories().subscribe(
      categories => this.categories = categories
    );
  }

  saveRecipe(){
    // this._recipeService.postRecipe(this.recipeInput).subscribe();
  }
}
