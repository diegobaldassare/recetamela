import {Component, Input, OnInit} from '@angular/core';
import {MediaService} from "../../shared/services/media.service";
import {DomSanitizer} from "@angular/platform-browser";
import {RecipeStep} from "../../shared/models/recipe/recipe-step";
import {RecipeFormContainer} from "./recipe-form-container";
declare var $: any;

@Component({
  selector: 'app-recipe-form',
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.css']
})
export class RecipeFormComponent implements OnInit {
  @Input() private container: RecipeFormContainer;
  @Input() private submitText: string;
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
    return /^(https?:\/\/(www\.)?)?youtube\.com\/watch\?v=[a-zA-Z0-9_]+$/.test(this.container.recipe.videoUrl);
  }

  private get videoThumbnailUrl(): string {
    const split = this.container.recipe.videoUrl.split('v=');
    return `http://img.youtube.com/vi/${split[1]}/0.jpg`;
  }

  private get imageButtonText(): string {
    if (this.uploadingImage) return 'Subiendo';
    else return 'Agregar';
  }

  private get stepImageButtonText(): string {
    if (this.uploadingImage) return 'Subiendo imágen';
    else return 'Seleccionar imágen';
  }

  private get disabledImageButton(): boolean {
    return this.uploadingImage || this.container.recipe.images.length >= 10;
  }

  private get disabledSubmit():boolean {
    return this.sending ||
      (this.container.recipe.videoUrl.length > 0 && !this.validVideoUrl) ||
      this.container.recipe.description.trim().length == 0 ||
      !this.isAlphaNumSpaceNotEmpty(this.container.recipe.name.trim()) ||
      this.container.recipe.steps.length == 0 ||
      Object.keys(this.container.selectedIngredients).length == 0 ||
      Object.keys(this.container.selectedCategories).length == 0 ||
      this.uploadingImage ||
      this.container.recipe.images.length == 0 ||
      this.container.recipe.servings < 1 ||
      this.container.recipe.duration < 2;
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
      this.container.recipe.steps[this.stepWhoseImageIsBeingUploaded].image = media;
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
      this.container.recipe.images.push(media)
    }, () => { (<HTMLInputElement> document.getElementById('image')).value = ''; });
  }

  private removeImage() {
    const $carousel = $('#general-images');
    const current = $('div.active').index();
    this.container.recipe.images.splice(current, 1);
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
      this.container.recipe.steps.push(step);
      this.stepDescription = '';
    }
  }

  private selectCategory() {
    const c = this.categoryName.toLowerCase().trim();
    if (!this.isAlphaNumSpaceNotEmpty(c)) return;
    if (this.container.categories[c]) {
      this.container.selectedCategories[c] = this.container.categories[c];
      delete this.container.categories[c];
      this.categoryName = '';
    }
    else if (this.container.selectedCategories[c]) this.categoryName = '';
  }

  private deselectCategory(c: string) {
    this.container.categories[c] = this.container.selectedCategories[c];
    delete this.container.selectedCategories[c];
  }

  private selectIngredient() {
    const i = this.ingredientName.toLowerCase().trim();
    if (!this.isAlphaNumSpaceNotEmpty(i)) return;
    if (!this.container.selectedIngredients[i]) {
      this.container.selectedIngredients[i] = this.container.ingredients[i];
      delete this.container.ingredients[i];
    }
    this.ingredientName = '';
  }

  private deselectIngredient(i: string) {
    this.container.ingredients[i] = this.container.selectedIngredients[i];
    delete this.container.selectedIngredients[i];
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
    this.container.submit().catch(() => this.sending = false);
  }
}
