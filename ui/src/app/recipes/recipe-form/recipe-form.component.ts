import {Component, Input, OnInit} from '@angular/core';
import {MediaService} from "../../shared/services/media.service";
import {DomSanitizer} from "@angular/platform-browser";
import {RecipeStep} from "../../shared/models/recipe/recipe-step";
import {RecipeFormContainer} from "./recipe-form-container";
import {Ingredient} from "../../shared/models/recipe/ingredient";
import {FormatService} from "../../shared/services/format.service";
import {IngredientFormula} from "../../shared/models/recipe/ingredient-formula";
declare var $: any;

@Component({
  selector: 'app-recipe-form',
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.css']
})
export class RecipeFormComponent implements OnInit {
  @Input() public container: RecipeFormContainer;
  @Input() private submitText: string;
  private uploadingImage: boolean;
  public stepDescription: string = '';
  public categoryName: string = '';
  public ingredientName: string = '';
  private sending: boolean;
  private stepWhoseImageIsBeingUploaded: number;

  constructor(
    private mediaService: MediaService,
    public sanitizer: DomSanitizer,
    private formatter: FormatService
  ) {}

  ngOnInit() {}

  public get submitButtonText(): string {
    return this.sending ? 'Enviando' : this.submitText;
  }

  public get validVideoUrl(): boolean {
    return /^(https?:\/\/(www\.)?)?youtube\.com\/watch\?v=[a-zA-Z0-9_-]+$/.test(this.container.recipe.videoUrl);
  }

  private get videoThumbnailUrl(): string {
    const split = this.container.recipe.videoUrl.split('v=');
    return `http://img.youtube.com/vi/${split[1]}/0.jpg`;
  }

  public get imageButtonText(): string {
    if (this.uploadingImage) return 'Subiendo';
    else return 'Agregar';
  }

  private get stepImageButtonText(): string {
    if (this.uploadingImage) return 'Subiendo imágen';
    else return 'Seleccionar imágen';
  }

  public get disabledImageButton(): boolean {
    return this.uploadingImage || this.container.recipe.images.length >= 10;
  }

  public get disabledSubmit():boolean {
    return this.sending ||
      (this.container.recipe.videoUrl.length > 0 && !this.validVideoUrl) ||
      this.container.recipe.description.trim().length == 0 ||
      !this.formatter.isAlphaNumSpaceNotEmpty(this.container.recipe.name.trim()) ||
      this.container.recipe.steps.length == 0 ||
      Object.keys(this.container.selectedCategories).length == 0 ||
      this.uploadingImage ||
      this.container.recipe.images.length == 0 ||
      this.container.recipe.servings < 1 ||
      this.container.recipe.duration < 2 ||
      this.invalidIngredients();
  }

  private invalidIngredients(): boolean {
    if (Object.keys(this.container.selectedIngredients).length == 0) return true;
    const k = Object.keys(this.container.selectedIngredients);
    for (let i = 0; i < k.length; i++) {
      const f = this.container.selectedIngredients[k[i]];
      if (!f.quantity || !f.unit) return true;
    }
    return false;
  }

  private selectStepImage(i) {
    if (this.uploadingImage) return;
    this.stepWhoseImageIsBeingUploaded = i;
    document.getElementById('step-image').click();
  }

  public addStepImage(e: Event) {
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

  public addImage(e: Event) {
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

  public addStep() {
    const d = this.stepDescription.trim();
    if (d.length > 0) {
      const step = new RecipeStep();
      step.description = this.formatter.capitalizeFirstChar(d);
      this.container.recipe.steps.push(step);
      this.stepDescription = '';
    }
  }

  public selectCategory() {
    const c = this.categoryName.toLowerCase().trim();
    if (!this.formatter.isAlphaNumSpaceNotEmpty(c)) return;
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
    if (!this.formatter.isAlphaNumSpaceNotEmpty(i)) return;
    if (!this.container.selectedIngredients[i]) {
      if (this.container.ingredients[i]) {
        this.container.selectedIngredients[i] = new IngredientFormula(this.container.ingredients[i]);
        delete this.container.ingredients[i];
      }
      else {
        this.container.selectedIngredients[i] = new IngredientFormula(new Ingredient(i));
      }
    }
    this.ingredientName = '';
  }

  private deselectIngredient(i: string) {
    this.container.ingredients[i] = this.container.selectedIngredients[i].ingredient;
    delete this.container.selectedIngredients[i];
  }

  private clear() {
    this.stepDescription = '';
    this.categoryName = '';
    this.ingredientName = '';
    this.sending = false;
  }

  public submit() {
    this.sending = true;
    this.container.submit().catch(() => this.sending = false);
  }
}
