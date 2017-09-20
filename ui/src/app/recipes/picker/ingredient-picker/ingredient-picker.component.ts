import {Component, Input, OnInit} from '@angular/core';
import {Picker} from "../picker";
import {RecipeService} from "../../../shared/services/recipe.service";
import {FormatService} from "../../../shared/services/format.service";

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
    if (this.unpicked[i]) {
      this.picked[i] = this.unpicked[i];
      delete this.unpicked[i];
      this.name = '';
    }
    else if (this.picked[i]) this.name = '';
  }

  protected unpick(i: string) {
    this.unpicked[i] = this.picked[i];
    delete this.picked[i];
  }
}
