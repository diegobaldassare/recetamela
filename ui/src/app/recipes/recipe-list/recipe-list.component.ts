import {Component, Input, OnInit} from '@angular/core';
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeBook} from "../../shared/models/recipe/recipebook";

@Component({
  selector: 'app-recipe-list',
  templateUrl: './recipe-list.component.html',
  styleUrls: ['./recipe-list.component.css']
})
export class RecipeListComponent implements OnInit {

  @Input() recipes: Recipe[];
  @Input() recipeBook: RecipeBook;

  constructor() { }

  ngOnInit() {
  }

}
