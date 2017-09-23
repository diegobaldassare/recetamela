import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-to-recipe-book',
  templateUrl: './add-to-recipe-book.component.html',
  styleUrls: ['./add-to-recipe-book.component.css']
})
export class AddToRecipeBookComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  private addToRecipeBook(){
    console.log("Add to RB");
  }
}
