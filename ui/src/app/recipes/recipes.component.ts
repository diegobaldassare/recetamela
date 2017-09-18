import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-recipes',
  templateUrl: './recipes.component.html',
  styleUrls: ['./recipes.component.scss']
})
export class RecipesComponent implements OnInit {

  private recipeName: string = "";

  constructor(private router: Router) {}

  ngOnInit() {}

  private search(): void {
    this.router.navigate(["/recetas/buscar"], { queryParams: { name: this.recipeName } });
  }
}
