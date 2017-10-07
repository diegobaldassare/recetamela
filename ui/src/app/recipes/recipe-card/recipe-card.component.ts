import {Component, Input, OnInit} from '@angular/core';
import {Recipe} from "../../shared/models/recipe/recipe";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {RecipeBookService} from "../../shared/services/recipebook.service";
import {RecipeBook} from "../../shared/models/recipe/recipebook";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-recipe-card',
  templateUrl: './recipe-card.component.html',
  styleUrls: ['./recipe-card.component.css']
})
export class RecipeCardComponent implements OnInit {

  @Input() recipe: Recipe;
  @Input() recipeBook: RecipeBook;

  onRecipeBook: boolean;

  constructor(private router: Router,
              private route: ActivatedRoute,
              public toaster: ToasterService,
              private recipeBookService: RecipeBookService) { }

  ngOnInit(){
    if(this.recipeBook) this.onRecipeBook = true;
  }

  toRecipe(){
    this.router.navigate([`/recetas/${this.recipe.id}`]);
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
}
