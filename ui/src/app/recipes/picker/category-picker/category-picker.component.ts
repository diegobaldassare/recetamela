import {Component, Input} from '@angular/core';
import {RecipeService} from "../../../shared/services/recipe.service";
import {FormatService} from "../../../shared/services/format.service";
import {Picker} from "../picker";

@Component({
  selector: 'app-category-picker',
  templateUrl: './category-picker.component.html',
  styleUrls: ['./category-picker.component.css']
})
export class CategoryPickerComponent extends Picker {

  constructor(
    private recipeService: RecipeService,
    private formatter: FormatService
  ) {
    super();
    this.recipeService.getRecipeCategories().then((all) => {
      all.forEach(e => {
        if (!this.picked[e.name]) this.unpicked[e.name] = e
      });
    });
  }

  protected pick() {
    const c = this.name.toLowerCase().trim();
    if (!this.formatter.isAlphaNumSpaceNotEmpty(c)) return;
    if (this.unpicked[c]) {
      this.picked[c] = this.unpicked[c];
      delete this.unpicked[c];
      this.name = '';
    }
    else if (this.picked[c]) this.name = '';
  }

  protected unpick(c: string) {
    this.unpicked[c] = this.picked[c];
    delete this.picked[c];
  }
}
