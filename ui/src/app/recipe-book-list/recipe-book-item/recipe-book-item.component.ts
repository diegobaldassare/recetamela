import {Component, Input, OnInit} from '@angular/core';
import {RecipeBook} from "../../shared/models/recipe/recipebook";
import {RecipeBookService} from "../../shared/services/recipebook.service";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-recipe-book-item',
  templateUrl: './recipe-book-item.component.html',
  styleUrls: ['./recipe-book-item.component.css']
})
export class RecipeBookItemComponent implements OnInit {
  @Input() recipeBook: RecipeBook;
  @Input() recipeBooks: RecipeBook[] = [];

  constructor(private recipeBookService: RecipeBookService, public toaster: ToasterService) { }

  ngOnInit() {
  }

  private deleteRecipeBook(){
    this.recipeBookService.delete(this.recipeBook.id).then(() => {
      this.toaster.pop('success', 'Recetario Eliminado');
      var index = this.recipeBooks.indexOf(this.recipeBook, 0);
      if (index > -1) {
        this.recipeBooks.splice(index, 1);
      }
    }, () => {
      this.toaster.pop('error', 'No se ha podido eliminar el recetario');
    });
  }

}
