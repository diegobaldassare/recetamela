import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {RecipeService} from "../../shared/services/recipe.service";
import {ToasterService} from "angular2-toaster";
import {ActivatedRoute, Router} from "@angular/router";
import {Media} from "../../shared/models/media";

@Component({
  selector: 'app-edit-recipe',
  templateUrl: './edit-recipe.component.html',
  styleUrls: ['./edit-recipe.component.css']
})
export class EditRecipeComponent implements OnInit {
  private recipeInput: RecipeInput;
  private categoryNames: Set<string> = new Set();
  private selectedCategoryNames: Set<string> = new Set();
  private ingredientNames: Set<string> = new Set();
  private selectedIngredientNames: Set<string> = new Set();
  private images: Media[] = [];
  private id: string;
  private instance: EditRecipeComponent = this;
  private fetched: boolean;
  private recipeRoute: string;

  constructor(
    private _recipeService: RecipeService,
    public toaster: ToasterService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this._recipeService.getRecipe(this.id).then(recipe => {
      this.recipeInput = new RecipeInput();
      this.recipeInput.name = recipe.name;
      this.recipeInput.description = recipe.description;
      this.recipeInput.duration = recipe.duration;
      this.recipeInput.serves = recipe.serves;
      this.recipeInput.videoUrl = recipe.videoUrl || '';
      this.recipeInput.difficulty = recipe.difficulty + '';
      this.images = recipe.images;
      this.recipeInput.steps = recipe.steps;
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
      this.fetched = true;
    }, () => this.fetched = true);
  }

  private submit(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.recipeInput.categoryNames = Array.from(this.selectedCategoryNames);
      this.recipeInput.ingredientNames = Array.from(this.selectedIngredientNames);
      this.recipeInput.imageIds = this.images.map(image => image.id);
      this._recipeService.modifyRecipe(this.id, this.recipeInput).then(() => {
        this.toaster.pop('success', 'Receta modificada');
        this.recipeRoute = `/recetas/${this.id}`;
        window.scrollTo(0, 0)
        resolve();
        this.router.navigate([this.recipeRoute]);
      }, () => {
        this.toaster.pop('error', 'Receta no guardada');
        reject();
      });
    });
  }
}
