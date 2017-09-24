import {Component, Input, OnInit} from '@angular/core';
import {RecipeBook} from "../../shared/models/recipe/recipebook";
import {RecipeBookService} from "../../shared/services/recipebook.service";

@Component({
  selector: 'app-recipe-book-item',
  templateUrl: './recipe-book-item.component.html',
  styleUrls: ['./recipe-book-item.component.css']
})
export class RecipeBookItemComponent implements OnInit {
  @Input() recipeBook: RecipeBook;

  constructor(private recipeBookService: RecipeBookService) { }

  ngOnInit() {
  }

  private deleteRecepieBook(){
    this.recipeBookService.delete(this.recipeBook.id);    //Usar bien el promise
  }

}
