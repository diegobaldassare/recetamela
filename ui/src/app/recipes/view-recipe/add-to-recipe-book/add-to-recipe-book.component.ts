import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-add-to-recipe-book',
  templateUrl: './add-to-recipe-book.component.html',
  styleUrls: ['./add-to-recipe-book.component.css']
})
export class AddToRecipeBookComponent implements OnInit {

  recipeBooks: string[] = ["XS", "S", "M", "L", "XL"];
  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor() { }

  ngOnInit() {
  }

  private selectRecipeBook(name: String){
    console.log(name);
    this.closeBtn.nativeElement.click();
  }

  // private addToRecipeBook(){
  //   console.log("Add to RB");
  // }
}
