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
import { CreateRecipeComponent } from './recipes/create-recipe/create-recipe.component';
import {RecipeService} from "./shared/services/recipe.service";
import {HttpModule} from "@angular/http";
import {MediaService} from "./shared/services/media.service";
import { HomeComponent } from './home/home.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToasterModule} from 'angular2-toaster';

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    HomeComponent,
    NotFoundComponent,
    FooterComponent,
    NavComponent,
    RecipesComponent,
    ViewRecipeComponent,
    CreateRecipeComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    AppRouterModule,
    HttpModule,
    BrowserAnimationsModule,
    ToasterModule
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
