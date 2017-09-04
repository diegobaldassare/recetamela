import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from "./landing/landing.component";
import { NotFoundComponent } from "./not-found/not-found.component";
import {RecipesComponent} from "./recipes/recipes.component";
import {ViewRecipeComponent} from "./recipes/view-recipe/view-recipe.component";
import {HomeComponent} from "./home/home.component";
import {CreateRecipeComponent} from "./recipes/create-recipe/create-recipe.component";

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'home', component: HomeComponent},
  { path: 'recipes', component: RecipesComponent },
  { path: 'recipes/new', component: CreateRecipeComponent },
  { path: 'recipes/:id', component: ViewRecipeComponent },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRouterModule {}
