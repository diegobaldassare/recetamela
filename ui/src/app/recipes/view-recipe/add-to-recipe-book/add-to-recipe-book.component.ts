import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {RecipeBookService} from "../../../shared/services/recipebook.service";
import {RecipeBook} from "../../../shared/models/recipe/recipebook";
import {Recipe} from "../../../shared/models/recipe/recipe";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-add-to-recipe-book',
  templateUrl: './add-to-recipe-book.component.html',
  styleUrls: ['./add-to-recipe-book.component.css']
})
export class AddToRecipeBookComponent implements OnInit {

  recipeBooks: RecipeBook[] = [];
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() recipe: Recipe;
  recipeBook: RecipeBook = new RecipeBook();

  constructor(private recipeBookService: RecipeBookService,
              public toaster: ToasterService) { }

  ngOnInit() {
    this.recipeBookService.getUserRecipeBooks().then(recipeBooks => {
      this.recipeBooks = recipeBooks;
    })
  }

  private selectRecipeBook(recipeBookId: string){
    console.log("El id que estoy mandado del front es: " + recipeBookId);
    console.log("El id de la receta es: " + this.recipe.id);    // El recipe esta llegando bien


    this.recipeBookService.get(recipeBookId).then((recipeBook : RecipeBook)=> {
      this.recipeBook = recipeBook;
      this.recipeBook.recipes.push(this.recipe);
      this.recipeBookService.update(recipeBookId, this.recipeBook).then(() => {
        this.toaster.pop('success', 'Se ha agregado correctamente');
      }, () => {
        this.toaster.pop('error', 'No se ha podido agregar');
      });
    });


    this.closeBtn.nativeElement.click();
  }
}
