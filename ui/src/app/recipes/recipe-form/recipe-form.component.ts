import {Component, Input, OnInit} from '@angular/core';
import {MediaService} from "../../shared/services/media.service";
import {DomSanitizer} from "@angular/platform-browser";
import {RecipeStep} from "../../shared/models/recipe/recipe-step";
declare var $: any;

@Component({
  selector: 'app-recipe-form',
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.css']
})
export class RecipeFormComponent implements OnInit {
  @Input() parent;
  @Input() submitText;
  private uploadingImage: boolean;
  private stepDescription: string = '';
  private categoryName: string = '';
  private ingredientName: string = '';
  private sending: boolean;
  private stepWhoseImageIsBeingUploaded: number;

  constructor(
    private mediaService: MediaService,
    public sanitizer: DomSanitizer,
  ) {}

  ngOnInit() {}

  private get submitButtonText(): string {
    return this.sending ? 'Enviando' : this.submitText;
  }

  private get validVideoUrl(): boolean {
    return /^(https?:\/\/(www\.)?)?youtube\.com\/watch\?v=[a-zA-Z0-9_]+$/.test(this.parent.recipeInput.videoUrl);
  }

  private get videoThumbnailUrl(): string {
    const split = this.parent.recipeInput.videoUrl.split('v=');
    return `http://img.youtube.com/vi/${split[1]}/0.jpg`;
  }

  private get imageButtonText(): string {
    if (this.uploadingImage) return 'Subiendo';
    else return 'Agregar';
  }

  private get disabledImageButton(): boolean {
    return this.uploadingImage || this.parent.images.length >= 10;
  }

  private get disabledSubmit():boolean {
    return this.sending ||
      (this.parent.recipeInput.videoUrl.length > 0 && !this.validVideoUrl) ||
      this.parent.recipeInput.description.trim().length == 0 ||
      !this.isAlphaNumSpaceNotEmpty(this.parent.recipeInput.name.trim()) ||
      this.parent.selectedIngredientNames.size == 0 ||
      this.parent.recipeInput.steps.length == 0 ||
      this.parent.selectedCategoryNames.size == 0 ||
      this.uploadingImage ||
      this.parent.images.length == 0;
  }

  private selectStepImage(i) {
    if (this.uploadingImage) return;
    this.stepWhoseImageIsBeingUploaded = i;
    document.getElementById('step-image').click();
  }

  private addStepImage(e: Event) {
    e.preventDefault();
    this.uploadingImage = true;
    const files = (<HTMLInputElement> document.getElementById('step-image')).files;
    if (!files.length) return;
    this.mediaService.uploadMedia(files[0]).then(media => {
      this.uploadingImage = false;
      (<HTMLInputElement> document.getElementById('step-image')).value = '';
      this.parent.recipeInput.steps[this.stepWhoseImageIsBeingUploaded].image = media;
    }, () => { (<HTMLInputElement> document.getElementById('step-image')).value = ''; });
  }

  private addImage(e: Event) {
    e.preventDefault();
    this.uploadingImage = true;
    const files = (<HTMLInputElement> document.getElementById('image')).files;
    if (!files.length) return;
    this.mediaService.uploadMedia(files[0]).then(media => {
      this.uploadingImage = false;
      (<HTMLInputElement> document.getElementById('image')).value = '';
      this.parent.images.push(media)
    }, () => { (<HTMLInputElement> document.getElementById('image')).value = ''; });
  }

  private removeImage() {
    const $carousel = $('#general-images');
    const current = $('div.active').index();
    this.parent.images.splice(current, 1);
    let next;
    if (current == 0) next = 1;
    else next = current - 1;
    $carousel.find('.item').eq(next).addClass('active');
    $carousel.find('li').eq(next).addClass('active');
  }

  private addStep() {
    const d = this.stepDescription.trim();
    if (d.length > 0) {
      const step = new RecipeStep();
      step.description = d;
      this.parent.recipeInput.steps.push(step);
      this.stepDescription = '';
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
    this.stepDescription = '';
    this.categoryName = '';
    this.ingredientName = '';
    this.sending = false;
  }

  private submit() {
    this.sending = true;
    this.parent.submit().then(() => this.clear(), () => this.sending = false);
  }

  private deleteRecipe(){
    console.log("Delete Recipe");
  }
}
