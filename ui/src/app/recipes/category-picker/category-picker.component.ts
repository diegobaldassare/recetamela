import {Component, Input} from '@angular/core';
import {RecipeService} from "../../shared/services/recipe.service";
import {FormatService} from "../../shared/services/format.service";

@Component({
  selector: 'app-category-picker',
  templateUrl: './category-picker.component.html',
  styleUrls: ['./category-picker.component.css']
})
export class CategoryPickerComponent {

  @Input() private selected;
  @Input() private label;
  private all = {};
  private name: string = '';

  constructor(
    private recipeService: RecipeService,
    private formatter: FormatService
  ) {
    this.recipeService.getRecipeCategories().then((all) => {
      all.forEach(c => this.all[c.name] = c);
    });
  }

  public selectCategory() {
    const c = this.name.toLowerCase().trim();
    if (!this.formatter.isAlphaNumSpaceNotEmpty(c)) return;
    if (this.all[c]) {
      this.selected[c] = this.all[c];
      delete this.all[c];
      this.name = '';
    }
    else if (this.selected[c]) this.name = '';
  }

  private deselectCategory(c: string) {
    this.all[c] = this.selected[c];
    delete this.selected[c];
  }
}
