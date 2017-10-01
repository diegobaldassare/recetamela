import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {RecipeBookService} from "../../../shared/services/recipebook.service";
import {RecipeBook} from "../../../shared/models/recipe/recipebook";

@Component({
  selector: 'app-add-to-recipe-book',
  templateUrl: './add-to-recipe-book.component.html',
  styleUrls: ['./add-to-recipe-book.component.css']
})
export class AddToRecipeBookComponent implements OnInit {

  recipeBooks: RecipeBook[] = [];
  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(private recipeBookService: RecipeBookService) { }

  ngOnInit() {
    this.recipeBookService.getUserRecipeBooks().then(recipeBooks => {
      this.recipeBooks = recipeBooks;
    })
  }

  private selectRecipeBook(recipeBookId: String){
    console.log(recipeBookId);
    this.closeBtn.nativeElement.click();
  }
}
