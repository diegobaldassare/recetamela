import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {RecipeBook} from "../shared/models/recipe/recipebook";
import {RecipeBookService} from "../shared/services/recipebook.service";

@Component({
  selector: 'app-recipe-book-list',
  templateUrl: './recipe-book-list.component.html',
  styleUrls: ['./recipe-book-list.component.css']
})
export class RecipeBookListComponent implements OnInit {

  private recipeBookForm: FormGroup;

  recipeBooks: RecipeBook[] = [];

  constructor(private fb: FormBuilder, private recipeBookService: RecipeBookService) {

    this.recipeBookForm = fb.group({
      'recipeBookName': new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit() {
    this.recipeBookService.getUserRecipeBooks().then(recipeBooks => {
      this.recipeBooks = recipeBooks;
    })
  }

  private createRecipeBook(){
    let recipeBook = new RecipeBook();
    recipeBook.name = this.recipeBookForm.value.recipeBookName;
    this.recipeBookService.create(recipeBook);             //Usar bien el promise, y devolver el toaster de bien o mal
  }



}
