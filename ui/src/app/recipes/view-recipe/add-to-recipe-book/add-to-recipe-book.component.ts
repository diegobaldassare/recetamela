import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {RecipeBookService} from "../../../shared/services/recipebook.service";
import {RecipeBook} from "../../../shared/models/recipe/recipebook";
import {Recipe} from "../../../shared/models/recipe/recipe";
import {ToasterService} from "angular2-toaster";
import {Router} from "@angular/router";

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
              public toaster: ToasterService,
              private router: Router) { }

  ngOnInit() {
    this.recipeBookService.getUserRecipeBooks().then(recipeBooks => {
      this.recipeBooks = recipeBooks;
    })
  }

  private selectRecipeBook(recipeBookId: string) {
    this.recipeBookService.get(recipeBookId).then((recipeBook : RecipeBook)=> {
      this.recipeBook = recipeBook;
      var found = false;
      for(var i = 0; i < this.recipeBook.recipes.length; i++) {
        if (this.recipeBook.recipes[i].id == this.recipe.id) {
          this.toaster.pop('success', 'La receta ya fue añadida');
          found = true;
          break;
        }
      }
      if (!found) {
        this.recipeBook.recipes.push(this.recipe);
        this.recipeBookService.update(recipeBookId, this.recipeBook).then(() => {
          this.toaster.pop('success', 'Se ha agregado correctamente');
        }, () => {
          this.toaster.pop('error', 'No se ha podido agregar');
        });
      }
    });

    this.closeBtn.nativeElement.click();
  }

  goToRecipeBook(){
    this.closeBtn.nativeElement.click();
    this.router.navigate(['/recetarios']);
  }
}
