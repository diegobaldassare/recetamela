import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {RecipeBookService} from "../../shared/services/recipebook.service";
import {RecipeBook} from "../../shared/models/recipe/recipebook";

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.component.html',
  styleUrls: ['./recipe-book.component.css']
})
export class RecipeBookComponent implements OnInit {

  recipeBookId: string;
  recipeBook: RecipeBook = new RecipeBook();

  constructor(private route: ActivatedRoute, private recipeBookService: RecipeBookService) { }

  ngOnInit() {

    this.route.params
      .subscribe(
        (params: Params) => {
          this.recipeBookId = params['id'];
        }
      );

    this.recipeBookService.get(this.recipeBookId).then(recipeBook => {
      this.recipeBook = recipeBook;
    });
  }

}
