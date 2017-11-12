import {Component, Input, OnInit} from '@angular/core';
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeBookService} from "../../shared/services/recipebook.service";
import {RecipeBook} from "../../shared/models/recipe/recipebook";
import {ToasterService} from "angular2-toaster";
import {User} from "../../shared/models/user-model";
import {RecipeService} from "../../shared/services/recipe.service";
import {OnClickEvent} from "angular-star-rating";
import {RecipeRating} from "../../shared/models/recipe/recipe-rating";

@Component({
  selector: 'app-recipe-card',
  templateUrl: './recipe-card.component.html',
  styleUrls: ['./recipe-card.component.css']
})
export class RecipeCardComponent implements OnInit {

  @Input() recipe: Recipe;
  @Input() recipeBook: RecipeBook;
  @Input() onRecipeList: boolean;

  author: User;
  canRate: boolean;
  viewer: User = JSON.parse(localStorage.getItem("user"));
  private recipeRating: RecipeRating = new RecipeRating();

  onRecipeBook: boolean;

  constructor(public toaster: ToasterService,
              private recipeBookService: RecipeBookService,
              private recipeService: RecipeService) { }

  ngOnInit(){
    if(this.recipeBook) this.onRecipeBook = true;
    this.recipeService.getRatingFromUser(this.recipe.id).then(res => {
      this.recipeRating = res;
    });
    this.recipeService.getRecipeAuthor(this.recipe.id).then((res: User) => {
      this.author = res;
      if (this.viewer) this.canRate = this.author.id != this.viewer.id;
    });
  }

  deleteFromRecipeBook(){
    let index;
    for (let i=0; i<this.recipeBook.recipes.length; i++){
      if(this.recipeBook.recipes[i].id == this.recipe.id){
        index = i;
      }
    }
    if (index > -1) {
      this.recipeBook.recipes.splice(index, 1);
    }

    this.recipeBookService.update(this.recipeBook.id,this.recipeBook).then(() => {
      this.toaster.pop('success', 'Receta Eliminada');
    }, () => {
      this.toaster.pop('error', 'No se ha podido eliminar la receta');
    });

  }

  private addRating($event: OnClickEvent) {
    const rating = new RecipeRating();
    rating.rating = $event.rating;
    this.recipeService.addRating(this.recipe.id, rating).then(recipe => this.recipe = recipe);
  }
}
