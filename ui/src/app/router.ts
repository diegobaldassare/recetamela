import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from "./landing/landing.component";
import { NotFoundComponent } from "./not-found/not-found.component";
import {RecipesComponent} from "./recipes/recipes.component";
import {ViewRecipeComponent} from "./recipes/view-recipe/view-recipe.component";
import {CreateRecipeComponent} from "./recipes/create-recipe/create-recipe.component";
import {EditRecipeComponent} from "./recipes/edit-recipe/edit-recipe.component";

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'recetas', component: RecipesComponent },
  { path: 'recetas/crear', component: CreateRecipeComponent },
  { path: 'recetas/:id', component: ViewRecipeComponent },
  { path: 'recetas/:id/editar', component: EditRecipeComponent },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRouterModule {}
