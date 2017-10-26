import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotFoundComponent} from "./not-found/not-found.component";
import {RecipesComponent} from "./recipes/recipes.component";
import {ViewRecipeComponent} from "./recipes/view-recipe/view-recipe.component";
import {HomeComponent} from "./home/home.component";
import {CreateRecipeComponent} from "./recipes/create-recipe/create-recipe.component";
import {EditRecipeComponent} from "./recipes/edit-recipe/edit-recipe.component";
import {AuthGuard} from "./auth/authGuard.service";
import {NotFreeGuard} from "./auth/not-free-guard";
import {EditRecipeGuard} from "./auth/edit-recipe-guard";
import {ProfileComponent} from "./profile/profile.component";
import {SearchRecipesComponent} from "./recipes/search-recipes/search-recipes.component";
import {RecipeBookListComponent} from "./recipe-book-list/recipe-book-list.component";
import {RecipeBookComponent} from "./recipe-book-list/recipe-book/recipe-book.component";
import {ChefRequestComponent} from "./chef-request/chef-request.component";
import {NewsFeedComponent} from "./news/news-feed/news-feed.component";
import {NewsComponent} from "./news/news/news.component";
import { ChefGuard } from './auth/chef-guard.service';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'recetas', component: RecipesComponent, canActivate: [AuthGuard] },
  { path: 'recetas/crear', component: CreateRecipeComponent, canActivate: [AuthGuard, NotFreeGuard]},
  { path: 'recetas/buscar', component: SearchRecipesComponent, canActivate: [AuthGuard] },
  { path: 'recetas/:id', component: ViewRecipeComponent, canActivate: [AuthGuard] },
  { path: 'recetas/:id/editar', component: EditRecipeComponent, canActivate: [AuthGuard, NotFreeGuard, EditRecipeGuard] },
  { path: 'recetarios', component: RecipeBookListComponent, canActivate: [AuthGuard, NotFreeGuard]},
  { path: 'recetarios/:id', component: RecipeBookComponent, canActivate: [AuthGuard, NotFreeGuard]},
  { path: 'solicitudes', component: ChefRequestComponent, canActivate: [AuthGuard]},    //Tiene que ser AdminGuard
  { path: 'usuario/:id/perfil', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'noticias', component: NewsFeedComponent, canActivate: [AuthGuard, ChefGuard] },
  { path: 'noticias/:id', component: NewsComponent, canActivate: [AuthGuard] },
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
