import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {RecipeService} from "../../shared/services/recipe.service";
import {ToasterService} from "angular2-toaster";
import {Media} from "../../shared/models/media";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './create-recipe.component.html',
  styleUrls: ['./create-recipe.component.css']
})
export class CreateRecipeComponent implements OnInit {
  private recipeInput: RecipeInput = new RecipeInput();
  private categoryNames: Set<string> = new Set();
  private selectedCategoryNames: Set<string> = new Set();
  private ingredientNames: Set<string> = new Set();
  private selectedIngredientNames: Set<string> = new Set();
  private recipeRoute: string = '';
  private image: Media;
  private instance: CreateRecipeComponent = this;

  constructor(
    private _recipeService: RecipeService,
    public toaster: ToasterService,
    private router: Router
  ) {}

  ngOnInit() {
    const t = this;
    this._recipeService.getRecipeCategories().then((categories) => {
      categories.forEach(c => t.categoryNames.add(<string> c.name));
    });
    this._recipeService.getIngredients().then((ingredients) => {
      ingredients.forEach(c => t.ingredientNames.add(<string> c.name));
    });
  }

  private clear() {
    this.image = null;
    this.recipeInput = new RecipeInput();
    this.selectedIngredientNames.forEach(e => this.ingredientNames.add(e));
    this.selectedCategoryNames.forEach(e => this.categoryNames.add(e));
    this.selectedIngredientNames.clear();
    this.selectedCategoryNames.clear();
  }

  private submit(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.recipeInput.categoryNames = Array.from(this.selectedCategoryNames);
      this.recipeInput.ingredientNames = Array.from(this.selectedIngredientNames);
      this._recipeService.createRecipe(this.recipeInput).then(r => {
        this.clear();
        this.recipeRoute = `/recetas/${r.id}`;
        this.toaster.pop('success', 'Receta creada');
        window.scrollTo(0, 0);
        resolve();
        this.router.navigate([this.recipeRoute]);
      }, () => {
        this.toaster.pop('error', 'Receta no creada');
        reject();
      });
    });
  }
}
