import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {RecipeBookService} from "../../shared/services/recipebook.service";
import {RecipeBook} from "../../shared/models/recipe/recipebook";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.component.html',
  styleUrls: ['./recipe-book.component.css']
})
export class RecipeBookComponent implements OnInit {

  recipeBookId: string;
  recipeBook: RecipeBook = new RecipeBook();

  private recipeBookForm: FormGroup;

  constructor(private route: ActivatedRoute,
              private recipeBookService: RecipeBookService,
              public toaster: ToasterService,
              private cdRef: ChangeDetectorRef,
              private fb: FormBuilder) {

    this.recipeBookForm = fb.group({
      'recipeBookName': new FormControl(null, [Validators.required]),
    });
  }

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

  modifyRecipeBook(){
    console.log(this.recipeBookForm.value.recipeBookName);
    let newRecipeBook: RecipeBook = new RecipeBook();       //Fijarme si puedo igualar el nuevo al viejo y solo cambiar el nombre
    newRecipeBook.name = this.recipeBookForm.value.recipeBookName;
    newRecipeBook.recipes = this.recipeBook.recipes;
    newRecipeBook.id = this.recipeBookId;
    newRecipeBook.creator = this.recipeBook.creator;
    this.recipeBookService.update(this.recipeBookId, newRecipeBook).then(() => {    //No esta funcionando el update
      this.toaster.pop('success', 'Recetario Modificado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido modificar el recetario');
    });

    this.cdRef.detectChanges();
  }

}

