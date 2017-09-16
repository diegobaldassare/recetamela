import {Component, Input, OnInit} from '@angular/core';
import {MyAuthService} from "../../auth/my-auth-service";

@Component({
  selector: 'app-recipe-card',
  templateUrl: './recipe-card.component.html',
  styleUrls: ['./recipe-card.component.css']
})
export class RecipeCardComponent implements OnInit {

  @Input() recipe;


  constructor() { }

  ngOnInit() {
  }

}
