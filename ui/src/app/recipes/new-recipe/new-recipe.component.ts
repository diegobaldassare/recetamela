import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {Category} from "../../shared/models/recipe/category";
import {RecipeService} from "../../shared/services/recipe.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MediaService} from "../../shared/services/media.service";
import {Media} from "../../shared/models/media";
import {Ingredient} from "../../shared/models/recipe/ingredient";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.scss']
})
export class NewRecipeComponent implements OnInit {

  private recipeInput: RecipeInput = new RecipeInput();
  private image: Media;
  private uploadingImage: boolean;
  private categories; selectedCategories: Set<string> = new Set();
  private ingredients; selectedIngredients: Set<string> = new Set();
  private steps: String[] = [];
  private step; categoryName: string = '';

  constructor(
    private _recipeService: RecipeService,
    private _mediaService: MediaService,
    public sanitizer: DomSanitizer
  ) {
    this.categories = new Set();
  }

  ngOnInit() {
    const t = this;
    this._recipeService.getRecipeCategories().then((categories) => {
      categories.forEach(c => t.categories.add(c.name));
    });
  }

  public uploadImage(e: Event) {
    e.preventDefault();
    this.uploadingImage = true;
    const files = (<HTMLInputElement> document.getElementById('image')).files;
    if (!files.length) return;
    this._mediaService.uploadMedia(files[0]).then(media => {
      this.image = media;
      this.recipeInput.imageId = media.id;
      this.uploadingImage = false;
    });
  }

  public addStep() {
    this.steps.push(this.step);
    this.step = '';
  }

  public selectCategory() {
    if (!this.selectedCategories.has(this.categoryName)) {
      this.selectedCategories.add(this.categoryName);
      this.categories.delete(this.categoryName);
    }
    this.categoryName = '';
  }

  public deselectCategory(c: string) {
    this.selectedCategories.delete(c);
    this.categories.add(c);
  }

  public validateForm() {}

  private createRecipe() {
    this.recipeInput.steps = this.steps.join('\n');
    this.recipeInput.categoryNames = Array.from(this.selectedCategories);
    this.recipeInput.ingredientNames = Array.from(this.selectedIngredients);
    // this._recipeService.postRecipe(this.recipeInput).subscribe();
  }
}
