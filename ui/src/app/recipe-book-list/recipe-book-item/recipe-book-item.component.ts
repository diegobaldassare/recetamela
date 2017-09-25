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

  constructor(private recipeBookService: RecipeBookService, public toaster: ToasterService) { }

  ngOnInit() {
  }

  private deleteRecepieBook(){
    this.recipeBookService.delete(this.recipeBook.id).then(() => {
      this.toaster.pop('success', 'Recetario Eliminado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido eliminar el recetario');
    });
  }

}
