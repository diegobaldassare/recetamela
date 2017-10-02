import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { CookieService }   from 'angular2-cookie/services/cookies.service';
import {AppComponent} from './app.component';
import {DOMService} from './shared/dom.service';
import { AppRouterModule } from './router';
import { LandingComponent } from './landing/landing.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { FooterComponent } from './footer/footer.component';
import { NavComponent } from './nav/nav.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { RecipesComponent } from './recipes/recipes.component';
import { ViewRecipeComponent } from './recipes/view-recipe/view-recipe.component';
import { CreateRecipeComponent } from './recipes/create-recipe/create-recipe.component';
import {RecipeService} from "./shared/services/recipe.service";
import {HttpModule} from "@angular/http";
import {MediaService} from "./shared/services/media.service";
import { HomeComponent } from './home/home.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToasterModule} from 'angular2-toaster';
import { RecipeFormComponent } from './recipes/recipe-form/recipe-form.component';
import { EditRecipeComponent } from './recipes/edit-recipe/edit-recipe.component';
import { UpgradeComponent } from './nav/upgrade/upgrade.component';
import {LogInComponent} from "./log-in/log-in.component"
import {UserService} from "./shared/services/user.service";
import {AuthInterceptor} from "./auth/auth-interceptor";
import {MyAuthService} from "./auth/my-auth-service";
import {HttpService} from "./shared/services/http.service";
import { SharedService } from "./shared/services/shared.service";
import {RecipeCardComponent} from "./recipes/recipe-card/recipe-card.component";
import {NotLoggedComponent} from "./landing/not-logged/not-logged.component";
import {AuthGuard} from "./auth/authGuard.service";
import {UnauthorizedInterceptor} from "./auth/unauthorized-interceptor";
import {KeysPipe} from "./shared/pipes/keys-pipe";
import {RecipeListComponent} from "./recipes/recipe-list/recipe-list.component";
import {PremiumGuard} from "./auth/premium-guard";
import {CreditCardService} from "./shared/services/credit-card.service";
import {PaymentService} from "./shared/services/payment.service";
import {EditRecipeGuard} from "./auth/edit-recipe-guard";
import {ProfileComponent} from "./profile/profile.component";
import {MessageTimePipe} from "./shared/pipes/message-time.pipe";
import { SearchRecipesComponent } from './recipes/search-recipes/search-recipes.component';
import { CategoryPickerComponent } from './recipes/picker/category-picker/category-picker.component';
import {FormatService} from "./shared/services/format.service";
import { IngredientPickerComponent } from './recipes/picker/ingredient-picker/ingredient-picker.component';
import { AddToRecipeBookComponent } from './recipes/view-recipe/add-to-recipe-book/add-to-recipe-book.component';
import { RecipeBookListComponent } from './recipe-book-list/recipe-book-list.component';
import {RecipeBookComponent} from "./recipe-book-list/recipe-book/recipe-book.component";
import { RecipeBookItemComponent } from './recipe-book-list/recipe-book-item/recipe-book-item.component';
import {RecipeBookService} from "./shared/services/recipebook.service";
import { RecipeCategoryService } from "./shared/services/recipecategory.service";
import { CategoriesComponent } from './nav/categories/categories.component';


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
    RecipeFormComponent,
    EditRecipeComponent,
    UpgradeComponent,
    LogInComponent,
    RecipeCardComponent,
    RecipeListComponent,
    NotLoggedComponent,
    KeysPipe,
    ProfileComponent,
    MessageTimePipe,
    SearchRecipesComponent,
    CategoryPickerComponent,
    IngredientPickerComponent,
    AddToRecipeBookComponent,
    RecipeBookComponent,
    RecipeBookListComponent,
    RecipeBookItemComponent,
    CategoriesComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    AppRouterModule,
    BrowserAnimationsModule,
    ToasterModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: UnauthorizedInterceptor,
      multi: true,
    },
    CookieService,
    DOMService,
    RecipeService,
    MediaService,
    UserService,
    MyAuthService,
    HttpService,
    SharedService,
    AuthGuard,
    PremiumGuard,
    CreditCardService,
    EditRecipeGuard,
    FormatService,
    PaymentService,
    RecipeBookService,
    RecipeCategoryService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
