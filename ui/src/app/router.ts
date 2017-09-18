import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from "./not-found/not-found.component";
import {RecipesComponent} from "./recipes/recipes.component";
import {ViewRecipeComponent} from "./recipes/view-recipe/view-recipe.component";
import {HomeComponent} from "./home/home.component";
import {CreateRecipeComponent} from "./recipes/create-recipe/create-recipe.component";
import {EditRecipeComponent} from "./recipes/edit-recipe/edit-recipe.component";
import {AuthGuard} from "./auth/authGuard.service";
import {PremiumGuard} from "./auth/premium-guard";
import {EditRecipeGuard} from "./auth/edit-recipe-guard";
import {SearchRecipesComponent} from "./recipes/search-recipes/search-recipes.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'recetas', component: RecipesComponent, canActivate: [AuthGuard] },
  { path: 'recetas/crear', component: CreateRecipeComponent, canActivate: [AuthGuard, PremiumGuard]},
  { path: 'recetas/buscar', component: SearchRecipesComponent, canActivate: [AuthGuard] },
  { path: 'recetas/:id', component: ViewRecipeComponent, canActivate: [AuthGuard] },
  { path: 'recetas/:id/editar', component: EditRecipeComponent, canActivate: [AuthGuard, PremiumGuard, EditRecipeGuard] },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [
    RouterModule,
  ],
})
export class AppRouterModule {}
