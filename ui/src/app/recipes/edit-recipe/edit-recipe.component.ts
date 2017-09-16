import { Component, OnInit } from '@angular/core';
import {RecipeService} from "../../shared/services/recipe.service";
import {ToasterService} from "angular2-toaster";
import {ActivatedRoute, Router} from "@angular/router";
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeFormContainer} from "../recipe-form/recipe-form-container";
import {User} from "../../shared/models/user-model";

@Component({
  selector: 'app-edit-recipe',
  templateUrl: './edit-recipe.component.html',
  styleUrls: ['./edit-recipe.component.css']
})
export class EditRecipeComponent extends RecipeFormContainer implements OnInit {
  private fetched: boolean;

  constructor(
    private _recipeService: RecipeService,
    public toaster: ToasterService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    super();
  }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this._recipeService.getRecipe(id).then(recipe => {
      const viewer: User = JSON.parse(localStorage.getItem("user"));
      if (viewer.id != recipe.author.id) this.router.navigate([`/recetas/${id}`]);
      this.recipe = recipe;
      if (!this.recipe.videoUrl) this.recipe.videoUrl = "";
      this.recipe.difficulty += "";
      const t = this;
      recipe.categories.forEach(c => this.selectedCategories[c.name] = c);
      recipe.ingredients.forEach(i => this.selectedIngredients[i.name] = i);
      this._recipeService.getRecipeCategories().then((categories) => {
        categories.forEach(c => {
          if (!t.selectedCategories[c.name]) this.categories[c.name] = c;
        });
      });
      this._recipeService.getIngredients().then((ingredients) => {
        ingredients.forEach(i => {
          if (!t.selectedIngredients[i.name]) this.ingredients[i.name] = i;
        });
      });
      this.fetched = true;
    }, () => this.fetched = true);
  }

  private get recipeRoute(): string {
    return `/recetas/${this.recipe.id}`;
  }

  public submit(): Promise<Recipe> {
    return new Promise((resolve, reject) => {
      this.recipe.categories = Object.keys(this.selectedCategories).map(k => this.selectedCategories[k]);
      this.recipe.ingredients = Object.keys(this.selectedIngredients).map(k => this.selectedIngredients[k]);
      this._recipeService.modifyRecipe(this.recipe.id, this.recipe).then(() => {
        this.router.navigate([this.recipeRoute]);
        this.toaster.pop('success', 'Receta modificada');
        resolve();
      }, () => {
        this.toaster.pop('error', 'Receta no guardada');
        reject();
      });
    });
  }

  deleteRecipe() {
    this._recipeService.deleteRecipe(this.recipe.id).then(() => {
      this.router.navigate(['/home']);
    }, () => {
      this.toaster.pop('error', 'Receta no eliminada');
    });
  }
}
