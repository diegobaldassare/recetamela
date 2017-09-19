import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {RecipeSearchQuery} from "./recipe-search-query";
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeService} from "../../shared/services/recipe.service";
import {ToasterService} from "angular2-toaster";
import {User} from "../../shared/models/user-model";
import {RecipeCategory} from "../../shared/models/recipe/recipe-category";

@Component({
  selector: 'app-search-recipes',
  templateUrl: './search-recipes.component.html',
  styleUrls: ['./search-recipes.component.css']
})
export class SearchRecipesComponent implements OnInit {

  private viewer: User = JSON.parse(localStorage.getItem("user"));
  private query: RecipeSearchQuery = new RecipeSearchQuery;
  private results: Recipe[] = [];
  private difficulties = [
    { value: 0, display: "cualquiera" },
    { value: 1, display: "muy fácil" },
    { value: 2, display: "fácil" },
    { value: 3, display: "intermedio" },
    { value: 4, display: "dificil" },
    { value: 5, display: "muy dificil" },
  ];

  private selectedCategories: RecipeCategory[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private recipeService: RecipeService,
    private toast: ToasterService
  ) {
    const q = this.route.snapshot.queryParams;
    this.query.name = q['name'] || "";
    this.query.categoryNames = q['categoryNames'] || "";
    this.query.ingredientNames = q['ingredientNames'] || "";
    this.query.difficulty = q['difficulty'] || 0;
    this.query.authorName = q['authorName'] || "";
  }

  ngOnInit() {
    this.search();
  }

  private search(): void {
    if (this.emptyQuery()) return;
    this.recipeService.search(this.query)
      .then(r => {
          this.router.navigate([], { queryParams: this.query });
          this.results = r;
        }, () => this.toast.pop("error", "No se pudo efectuar la búsqueda"));
  }

  private emptyQuery(): boolean {
    return this.query.name == "" &&
      this.query.categoryNames == "" &&
      this.query.difficulty == 0 &&
      this.query.authorName == "";
  }
}
