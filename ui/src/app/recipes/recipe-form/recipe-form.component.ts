import {Component, Input, OnInit} from '@angular/core';
import {Media} from "../../shared/models/media";
import {MediaService} from "../../shared/services/media.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-recipe-form',
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.css']
})
export class RecipeFormComponent implements OnInit {
  @Input() parent;
  @Input() submitText;
  private uploadingImage: boolean;
  private step: string = '';
  private categoryName: string = '';
  private ingredientName: string = '';
  private sending: boolean;

  constructor(
    private _mediaService: MediaService,
    public sanitizer: DomSanitizer,
  ) {}

  ngOnInit() {}

  private get validVideoUrl():boolean {
    return /^(https?:\/\/(www\.)?)?youtube\.com\/watch\?v=[a-zA-Z0-9]+$/.test(this.parent.recipeInput.videoUrl);
  }

  private get videoThumbnailUrl(): string {
    const split = this.parent.recipeInput.videoUrl.split('v=');
    return `http://img.youtube.com/vi/${split[1]}/0.jpg`;
  }

  private get submitImageText(): string {
    if (this.uploadingImage) return 'Subiendo';
    else return this.parent.image ? 'Cambiar' : 'Seleccionar';
  }

  private get disabledSubmit():boolean {
    return this.sending ||
      (this.parent.recipeInput.videoUrl.length > 0 && !this.validVideoUrl) ||
      !this.isAlphaNumSpaceNotEmpty(this.parent.recipeInput.name.trim()) ||
      this.parent.selectedIngredientNames.size == 0 ||
      this.parent.recipeInput.steps.length == 0 ||
      this.parent.selectedCategoryNames.size == 0 ||
      this.uploadingImage ||
      !this.parent.image;
  }

  private uploadImage(e: Event) {
    e.preventDefault();
    this.uploadingImage = true;
    const files = (<HTMLInputElement> document.getElementById('image')).files;
    if (!files.length) return;
    this._mediaService.uploadMedia(files[0]).then(media => {
      this.parent.image = media;
      this.parent.recipeInput.imageId = media.id;
      this.uploadingImage = false;
    });
  }

  private addStep() {
    const s = this.step.trim();
    if (this.isAlphaNumSpaceNotEmpty(s)) {
      this.parent.recipeInput.steps.push(s);
      this.step = '';
    }
  }

  private selectCategory() {
    const c = this.categoryName.toLowerCase().trim();
    if (!this.isAlphaNumSpaceNotEmpty(c)) return;
    if (!this.parent.selectedCategoryNames.has(c)) {
      this.parent.selectedCategoryNames.add(c);
      this.parent.categoryNames.delete(c);
    }
    this.categoryName = '';
  }

  private deselectCategory(c: string) {
    this.parent.selectedCategoryNames.delete(c);
    this.parent.categoryNames.add(c);
  }

  private selectIngredient() {
    const i = this.ingredientName.toLowerCase().trim();
    if (!this.isAlphaNumSpaceNotEmpty(i)) return;
    if (!this.parent.selectedIngredientNames.has(i)) {
      this.parent.selectedIngredientNames.add(i);
      this.parent.ingredientNames.delete(i);
    }
    this.ingredientName = '';
  }

  private deselectIngredient(i: string) {
    this.parent.selectedIngredientNames.delete(i);
    this.parent.ingredientNames.add(i);
  }

  private isAlphaNumSpaceNotEmpty(s: string): boolean {
    return /^[A-Za-zñ\d\s]+$/.test(s);
  }

  private clear() {
    this.step = '';
    this.categoryName = '';
    this.ingredientName = '';
    this.sending = false;
    (<HTMLInputElement> document.getElementById('image')).value = '';
  }

  private submit() {
    this.sending = true;
    this.parent.submit().then(() => this.clear(), () => this.sending = false);
  }

  private deleteRecipe(){
    console.log("Delete Recipe");
  }
}
