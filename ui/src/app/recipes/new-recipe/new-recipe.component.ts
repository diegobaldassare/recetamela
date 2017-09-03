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
  styleUrls: ['./new-recipe.component.css']
})
export class NewRecipeComponent implements OnInit {

  private recipeInput: RecipeInput;
  private categories: Category[];
  private image: Media;

  constructor(
    private _recipeService: RecipeService,
    private _mediaService: MediaService,
    public sanitizer: DomSanitizer) {
    this.recipeInput = new RecipeInput();
  }

  ngOnInit() {
    this._recipeService.getRecipeCategories().subscribe(
      categories => this.categories = categories
    );
  }

  public uploadImage(e: Event) {
    e.preventDefault();
    const files = (<HTMLInputElement> document.getElementById('image')).files;
    if (!files.length) return;
    this._mediaService.uploadMedia(files[0]).then(media => {
      this.image = media;
      this.recipeInput.imageId = media.id;
    });
  }

  saveRecipe(){
    // this._recipeService.postRecipe(this.recipeInput).subscribe();
  }
}
