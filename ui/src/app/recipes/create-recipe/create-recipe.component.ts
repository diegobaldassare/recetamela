import { Component, OnInit } from '@angular/core';
import {RecipeService} from "../../shared/services/recipe.service";
import {ToasterService} from "angular2-toaster";
import {Router} from "@angular/router";
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeFormContainer} from "../recipe-form/recipe-form-container";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './create-recipe.component.html',
  styleUrls: ['./create-recipe.component.css']
})
export class CreateRecipeComponent extends RecipeFormContainer implements OnInit {
  constructor(
    private _recipeService: RecipeService,
    public toaster: ToasterService,
    private router: Router
  ) {
    super();
    this.recipe = new Recipe();
  }

  ngOnInit() {
    this._recipeService.getRecipeCategories().then((categories) => {
      categories.forEach(c => this.categories[c.name] = c);
    });
    this._recipeService.getIngredients().then((ingredients) => {
      ingredients.forEach(i => this.ingredients[i.name] = i);
    });
  }

  public submit(): Promise<Recipe> {
    return new Promise((resolve, reject) => {
      this.recipe.categories = Object.keys(this.selectedCategories).map(k => this.selectedCategories[k]);
      this.recipe.ingredients = Object.keys(this.selectedIngredients).map(k => this.selectedIngredients[k]);
      this._recipeService.createRecipe(this.recipe).then(r => {
        this.router.navigate([`/recetas/${r.id}`]);
        this.toaster.pop('success', 'Receta creada');
        resolve();
      }, () => {
        this.toaster.pop('error', 'Receta no creada');
        reject();
      });
    });
  }
}
