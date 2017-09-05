import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {RecipeService} from "../../shared/services/recipe.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MediaService} from "../../shared/services/media.service";
import {Media} from "../../shared/models/media";
import {ToasterService} from "angular2-toaster";

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
  private step: string = '';
  private categoryName: string = '';
  private ingredientName: string = '';
  private createdRecipeRoute: string = '';
  private sending: boolean;

  constructor(
    private _recipeService: RecipeService,
    private _mediaService: MediaService,
    public sanitizer: DomSanitizer,
    public toaster: ToasterService
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

  private get disabledSubmit():boolean {
    return this.sending ||
      (this.recipeInput.videoUrl.length > 0 &&
        this.recipeInput.videoUrl.trim().length == 0) ||
      !this.isAlphaNumSpaceNotEmpty(this.recipeInput.name.trim()) ||
      this.selectedIngredients.size == 0 ||
      this.recipeInput.steps.length == 0 ||
      this.selectedCategories.size == 0 ||
      this.uploadingImage ||
      !this.image;
  }

  private uploadImage(e: Event) {
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

  private addStep() {
    const s = this.step.trim();
    if (this.isAlphaNumSpaceNotEmpty(s)) {
      this.recipeInput.steps.push(s);
      this.step = '';
    }
  }

  private selectCategory() {
    const c = this.categoryName.toLowerCase().trim();
    if (!this.isAlphaNumSpaceNotEmpty(c)) return;
    if (!this.selectedCategories.has(c)) {
      this.selectedCategories.add(c);
      this.categories.delete(c);
    }
    this.categoryName = '';
  }

  private deselectCategory(c: string) {
    this.selectedCategories.delete(c);
    this.categories.add(c);
  }

  private selectIngredient() {
    const i = this.ingredientName.toLowerCase().trim();
    if (!this.isAlphaNumSpaceNotEmpty(i)) return;
    if (!this.selectedIngredients.has(i)) {
      this.selectedIngredients.add(i);
      this.ingredients.delete(i);
    }
    this.ingredientName = '';
  }

  private deselectIngredient(i: string) {
    this.selectedIngredients.delete(i);
    this.ingredients.add(i);
  }

  private isAlphaNumSpaceNotEmpty(s: string): boolean {
    return /^[A-Za-zñ\d\s]+$/.test(s);
  }

  private clear() {
    this.recipeInput = new RecipeInput();
    this.image = null;
    this.step = '';
    this.categoryName = '';
    this.ingredientName = '';
    this.selectedIngredients.forEach(e => this.ingredients.add(e));
    this.selectedCategories.forEach(e => this.categories.add(e));
    this.selectedIngredients.clear();
    this.selectedCategories.clear();
    (<HTMLInputElement> document.getElementById('image')).value = '';
  }

  private createRecipe() {
    this.sending = true;
    this.recipeInput.categoryNames = Array.from(this.selectedCategories);
    this.recipeInput.ingredientNames = Array.from(this.selectedIngredients);
    this._recipeService.createRecipe(this.recipeInput).then(r => {
      this.clear();
      this.sending = false;
      this.createdRecipeRoute = `/recetas/${r.id}`;
      window.scrollTo(0, 0);
    }, () => {
      this.toaster.pop('error', 'Receta no creada');
      this.sending = false;
    });
  }
}
