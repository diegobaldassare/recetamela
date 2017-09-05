import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {RecipeService} from "../../shared/services/recipe.service";
import {ToasterService} from "angular2-toaster";
import {ActivatedRoute} from "@angular/router";
import {Media} from "../../shared/models/media";

@Component({
  selector: 'app-edit-recipe',
  templateUrl: './edit-recipe.component.html',
  styleUrls: ['./edit-recipe.component.css']
})
export class EditRecipeComponent implements OnInit {
  private recipeInput: RecipeInput = new RecipeInput();
  private categoryNames: Set<string> = new Set();
  private selectedCategoryNames: Set<string> = new Set();
  private ingredientNames: Set<string> = new Set();
  private selectedIngredientNames: Set<string> = new Set();
  private image: Media;
  private id: string;
  private instance = this;

  constructor(
    private _recipeService: RecipeService,
    public toaster: ToasterService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this._recipeService.getRecipe(this.id).then(recipe => {
      this.recipeInput.name = recipe.name;
      this.recipeInput.description = recipe.description;
      this.recipeInput.videoUrl = recipe.videoUrl || '';
      this.recipeInput.difficulty = recipe.difficulty + '';
      this.image = recipe.image;
      this.recipeInput.imageId = recipe.image.id;
      this.recipeInput.steps = recipe.steps.split('\n');
      const t = this;
      recipe.categories.forEach(c => t.selectedCategoryNames.add(c.name));
      recipe.ingredients.forEach(i => t.selectedIngredientNames.add(i.name));
      this._recipeService.getRecipeCategories().then((categories) => {
        categories.forEach(c => {
          const name = <string> c.name;
          if (!t.selectedCategoryNames.has(name)) t.categoryNames.add(name);
        });
      });
      this._recipeService.getIngredients().then((ingredients) => {
        ingredients.forEach(c => {
          const name = <string> c.name;
          if (!t.selectedIngredientNames.has(name)) t.ingredientNames.add(name);
        });
      });
    }, () => {});
  }

  private submit(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.recipeInput.categoryNames = Array.from(this.selectedCategoryNames);
      this.recipeInput.ingredientNames = Array.from(this.selectedIngredientNames);
      this._recipeService.modifyRecipe(this.id, this.recipeInput).then(() => {
        this.toaster.pop('success', 'Receta guardada');
        resolve();
      }, () => {
        this.toaster.pop('error', 'Receta no guardada');
        reject();
      });
    });
  }
}
