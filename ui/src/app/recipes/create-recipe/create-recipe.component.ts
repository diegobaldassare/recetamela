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
  private steps: string[] = [];
  private step: string = '';
  private categoryName: string = '';
  private ingredientName: string = '';
  private createdRecipeRoute: string = '';

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

  private get invalidForm():boolean {
    return this.recipeInput.name == '' ||
      this.recipeInput.description == '' ||
      this.selectedIngredients.size == 0 ||
      this.steps.length == 0 ||
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
    this.steps.push(this.step);
    this.step = '';
  }

  private selectCategory() {
    if (!this.selectedCategories.has(this.categoryName)) {
      this.selectedCategories.add(this.categoryName);
      this.categories.delete(this.categoryName);
    }
    this.categoryName = '';
  }

  private deselectCategory(c: string) {
    this.selectedCategories.delete(c);
    this.categories.add(c);
  }

  private selectIngredient() {
    if (!this.selectedIngredients.has(this.ingredientName)) {
      this.selectedIngredients.add(this.ingredientName);
      this.ingredients.delete(this.ingredientName);
    }
    this.ingredientName = '';
  }

  private deselectIngredient(i: string) {
    this.selectedIngredients.delete(i);
    this.ingredients.add(i);
  }

  private clearForm() {
    this.recipeInput = new RecipeInput();
    this.image = null;
    this.steps = [];
    this.step = '';
    this.categoryName = '';
    this.ingredientName = '';
    this.selectedIngredients.forEach(e => this.ingredients.add(e));
    this.selectedCategories.forEach(e => this.categories.add(e));
    this.selectedIngredients.clear();
    this.selectedCategories.clear();
  }

  private createRecipe() {
    this.recipeInput.steps = this.steps.join('\n');
    this.recipeInput.categoryNames = Array.from(this.selectedCategories);
    this.recipeInput.ingredientNames = Array.from(this.selectedIngredients);
    this._recipeService.createRecipe(this.recipeInput).then(r => {
      this.clearForm();
      this.createdRecipeRoute = `/recetas/${r.id}`;
      window.scrollTo(0, 0);
    }, () => this.toaster.pop('error', 'Receta no creada'));
  }
}
