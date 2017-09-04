import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {RecipeService} from "../../shared/services/recipe.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MediaService} from "../../shared/services/media.service";
import {Media} from "../../shared/models/media";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './create-recipe.component.html',
  styleUrls: ['./create-recipe.component.scss']
})
export class CreateRecipeComponent implements OnInit {

  private recipeInput: RecipeInput = new RecipeInput();
  private image: Media;
  private uploadingImage: boolean;
  private categories: Set<string> = new Set();
  private selectedCategories: Set<string> = new Set();
  private ingredients: Set<string> = new Set();
  private selectedIngredients: Set<string> = new Set();
  private steps: string[] = [];
  private step: string = '';
  private categoryName: string = '';
  private ingredientName: string = '';

  constructor(
    private _recipeService: RecipeService,
    private _mediaService: MediaService,
    public sanitizer: DomSanitizer
  ) {}

  ngOnInit() {
    const t = this;
    this._recipeService.getRecipeCategories().then((categories) => {
      categories.forEach(c => t.categories.add(<string> c.name));
    });
    this._recipeService.getIngredients().then((ingredients) => {
      ingredients.forEach(c => t.ingredients.add(<string> c.name));
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

  public selectIngredient() {
    if (!this.selectedIngredients.has(this.ingredientName)) {
      this.selectedIngredients.add(this.ingredientName);
      this.ingredients.delete(this.ingredientName);
    }
    this.ingredientName = '';
  }

  public deselectIngredient(i: string) {
    this.selectedIngredients.delete(i);
    this.ingredients.add(i);
  }

  public validateForm() {}

  private createRecipe() {
    this.recipeInput.steps = this.steps.join('\n');
    this.recipeInput.categoryNames = Array.from(this.selectedCategories);
    this.recipeInput.ingredientNames = Array.from(this.selectedIngredients);
    // this._recipeService.createRecipe(this.recipeInput);
  }
}
