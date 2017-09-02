import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css']
})
export class NewRecipeComponent implements OnInit {

  private recipe;
  private ingredients;
  private categories;

  constructor() { }

  ngOnInit() {
  }


  /**
   * Save recipe.
   */
  saveRecipe(){

  }

}
