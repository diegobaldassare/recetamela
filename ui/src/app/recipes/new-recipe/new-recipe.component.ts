import { Component, OnInit } from '@angular/core';
import {RecipeInput} from "../../shared/models/recipe/recipe-input";
import {Category} from "../../shared/models/recipe/category";
import {RecipeService} from "../../shared/services/recipe.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MediaService} from "../../shared/services/media.service";
import {Media} from "../../shared/models/media";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.scss']
})
export class NewRecipeComponent implements OnInit {

  private recipeInput: RecipeInput;
  private categories: Category[];
  private image: Media;
  private uploadingImage: boolean;
  private steps: String[];

  constructor(
    private _recipeService: RecipeService,
    private _mediaService: MediaService,
    public sanitizer: DomSanitizer) {
    this.recipeInput = new RecipeInput();
    this.steps = [];
  }

  ngOnInit() {
    this._recipeService.getRecipeCategories().subscribe(
      categories => this.categories = categories
    );
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

  saveRecipe() {
    this.recipeInput.steps = this.steps.join('\n');
    // this._recipeService.postRecipe(this.recipeInput).subscribe();
  }
}
