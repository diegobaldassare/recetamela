import {Component, Input, OnInit} from '@angular/core';
import {Picker} from "../picker";
import {RecipeService} from "../../../shared/services/recipe.service";
import {FormatService} from "../../../shared/services/format.service";
import {Ingredient} from "../../../shared/models/recipe/ingredient";

@Component({
  selector: 'app-ingredient-picker',
  templateUrl: './ingredient-picker.component.html',
  styleUrls: ['./ingredient-picker.component.css']
})
export class IngredientPickerComponent extends Picker {

  constructor(
    private recipeService: RecipeService,
    private formatter: FormatService
  ) {
    super();
    this.recipeService.getIngredients().then((all) => {
      all.forEach(e => {
        if (!this.picked[e.name]) this.unpicked[e.name] = e
      });
    });
  }

  protected pick() {
    const i = this.name.toLowerCase().trim();
    if (!this.formatter.isAlphaNumSpaceNotEmpty(i)) return;
    if (!this.picked[i]) {
      if (this.unpicked[i]) {
        this.picked[i] = this.unpicked[i];
        delete this.unpicked[i];
      }
      else {
        const ingredient = new Ingredient();
        ingredient.name = i;
        this.picked[i] = ingredient;
      }
    }
    this.name = '';
  }

  protected unpick(i: string) {
    this.unpicked[i] = this.picked[i];
    delete this.picked[i];
  }
}
