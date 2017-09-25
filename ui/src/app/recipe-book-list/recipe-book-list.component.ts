import {ChangeDetectorRef, Component, NgZone, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {RecipeBook} from "../shared/models/recipe/recipebook";
import {RecipeBookService} from "../shared/services/recipebook.service";
import {SharedService} from "../shared/services/shared.service";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-recipe-book-list',
  templateUrl: './recipe-book-list.component.html',
  styleUrls: ['./recipe-book-list.component.css']
})
export class RecipeBookListComponent implements OnInit {

  private recipeBookForm: FormGroup;

  recipeBooks: RecipeBook[] = [];

  constructor(private fb: FormBuilder,
              private recipeBookService: RecipeBookService,
              public toaster: ToasterService,
              private cdRef: ChangeDetectorRef) {

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
    this.recipeBookService.create(recipeBook).then(() => {
      this.toaster.pop('success', 'Recetario Creado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido crear el recetario');
    });

    this.cdRef.detectChanges();
  }



}
