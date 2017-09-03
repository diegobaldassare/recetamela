import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import { DomSanitizer } from '@angular/platform-browser';
import {Recipe} from "../../shared/models/recipe/recipe";

@Component({
  selector: 'app-view-recipe',
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.css']
})
export class ViewRecipeComponent implements OnInit {

  private recipe: Recipe;
  private fetched;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    public sanitizer: DomSanitizer
  ){}

  ngOnInit() {
    const id = +this.route.snapshot.params['id'];
    this.http.get(`http://localhost:9000/api/recipe/${id}`).subscribe((recipe: Recipe) => {
      this.recipe = recipe;
      this.fetched = true;
    }, () => { this.fetched = true });
  }
}
