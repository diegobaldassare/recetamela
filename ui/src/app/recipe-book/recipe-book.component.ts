import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.component.html',
  styleUrls: ['./recipe-book.component.css']
})
export class RecipeBookComponent implements OnInit {

  private recipeBookForm: FormGroup;

  constructor(private fb: FormBuilder) {

    this.recipeBookForm = fb.group({
      'recipeBookName': new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit() {
  }

  private createRecipeBook(){
    const recipeBookName = this.recipeBookForm.value.recipeBookName;
    console.log(recipeBookName);
  }
}
