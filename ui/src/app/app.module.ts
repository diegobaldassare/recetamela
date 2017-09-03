import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { CookieService }   from 'angular2-cookie/services/cookies.service';
import {AppComponent} from './app.component';
import {AuthService} from './auth/auth.service';
import {DOMService} from './shared/dom.service';
import { AppRouterModule } from './router';
import { LandingComponent } from './landing/landing.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { FooterComponent } from './footer/footer.component';
import { NavComponent } from './nav/nav.component';
import {HttpClientModule} from "@angular/common/http";
import { RecipesComponent } from './recipes/recipes.component';
import { ViewRecipeComponent } from './recipes/view-recipe/view-recipe.component';
import { NewRecipeComponent } from './recipes/new-recipe/new-recipe.component';
import {RecipeService} from "./shared/services/recipe.service";
import {HttpModule} from "@angular/http";
import {MediaService} from "./shared/services/media.service";

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    NotFoundComponent,
    FooterComponent,
    NavComponent,
    RecipesComponent,
    ViewRecipeComponent,
    NewRecipeComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    AppRouterModule,
    HttpModule,
  ],
  providers: [
    CookieService,
    AuthService,
    DOMService,
    RecipeService,
    MediaService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
