import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from "./landing/landing.component";
import { NotFoundComponent } from "./not-found/not-found.component";
import {RecipesComponent} from "./recipes/recipes.component";
import {ViewRecipeComponent} from "./recipes/view-recipe/view-recipe.component";
import {NewRecipeComponent} from "./recipes/new-recipe/new-recipe.component";

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'recipes', component: RecipesComponent },
  { path: 'recipes/new', component: NewRecipeComponent },
  { path: 'recipes/:id', component: ViewRecipeComponent },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRouterModule {}
