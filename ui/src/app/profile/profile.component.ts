import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {RecipeService} from "../shared/services/recipe.service";
import {Recipe} from "../shared/models/recipe/recipe";
import {RecipeCategory} from "../shared/models/recipe/recipe-category";
import {RecipeCategoryService} from "../shared/services/recipecategory.service";
import {FormatService} from "../shared/services/format.service";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {ToasterService} from "angular2-toaster";
import {MyAuthService} from "../auth/my-auth-service";
import {News} from "../shared/models/news";
import {NewsService} from "../shared/services/news-service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {


  private user: User;
  private loggedUser: User;

  news: News[] = [];
  recipes: Recipe[] = [];
  followers: User[] = [];
  subscribed: boolean;

  private categories: RecipeCategory[] = [];
  private resultCategories: RecipeCategory[] = [];
  private categoryQuery: string = "";

  private following: User[] = [];
  private resultUsers: User[] = [];
  private usersQuery: string = "";

  private profileForm: FormGroup;
  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(
    public toaster: ToasterService,
    private route: ActivatedRoute,
    private userService: UserService,
    private recipeService: RecipeService,
    private recipeCategoryService: RecipeCategoryService,
    private router: Router,
    private formBuilder: FormBuilder,
    private formatter: FormatService,
    private myAuthService: MyAuthService,
    private newsService: NewsService) {

    const atLeastOne = (validator: ValidatorFn) => (
      group: FormGroup,
    ): ValidationErrors | null => {
      const hasAtLeastOne = group && group.controls && Object.keys(group.controls)
          .some(k => !validator(group.controls[k]));

      return hasAtLeastOne ? null : {
        atLeastOne: true,
      };
    };

    this.profileForm = formBuilder.group({
      'name': [''],
      'lastName': [''],
      'email': ['', [ProfileComponent.checkEmail]],
    }, { validator: atLeastOne(Validators.required) }
    );
  }

  ngOnInit() {
    this.route.params
      .subscribe(
      (params: Params) => {
        const id = params['id'];
        this.userService.getUser(id).then(user => {
          this.user = user;
          if (user.email == "recetamelareceta@gmail.com") {
            this.router.navigate(['/**']);
            return;
          }
          this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
          this.fetchRecipes();
          this.fetchFollowers();
          this.fetchFollowing();
          this.fetchCategories();
          this.fetchUnFollowedCategories();
          this.fetchNews();
        }).catch(err => this.router.navigate(['/**']));
      }
    );
  }

  private subscribe() {
    this.userService.followUser(this.user.id).subscribe((res : User) => {
      this.followers.push(res);
      this.subscribed = true;
      this.toaster.pop('success', 'Subscripto');
    }, () => {
      this.toaster.pop('error', 'No se ha podido subscribir');
    });
  }

  private unSubscribe() {
    this.userService.unFollowUser(this.user.id).subscribe((res : User) => {
      const index = this.followers.map(u => u.id).indexOf(res.id, 0);
      if (index > -1) {
        this.followers.splice(index, 1);
        this.subscribed = false;
        this.toaster.pop('success', 'Desubscripto');
      }
    }, () => {
      this.toaster.pop('error', 'No se ha podido desubscribir');
    });
  }

  private fetchRecipes() {
    this.recipeService.getUserRecipes(this.route.snapshot.params['id']).then((recipes: Recipe[]) =>
      this.recipes = recipes
    );
  }

  private fetchFollowers() {
    this.userService.getFollowers(this.route.snapshot.params['id']).subscribe((res : User[]) => {
      this.followers = res;
      this.subscribed = (this.followers.map(u => u.id).indexOf(this.loggedUser.id, 0) > -1);
    });
  }

  private fetchFollowing() {
    this.userService.getFollowing(this.route.snapshot.params['id']).subscribe((res : User[]) => {
      this.following = res;
    });
  }

  private followerClickListener(i: number) {
    this.router.navigate([`/usuario/${this.followers[i].id}/perfil`]);
  }

  private followingClickListener(i: number) {
    this.router.navigate([`/usuario/${this.following[i].id}/perfil`]);
  }

  private userClickListener(i: number) {
    this.router.navigate([`/usuario/${this.resultUsers[i].id}/perfil`]);
  }

  private fetchCategories() {
    this.recipeCategoryService.getUserCategories(this.route.snapshot.params['id']).subscribe((res : RecipeCategory[]) => {
      this.categories = res;
    });
  }

  private subscribeToCategory(index: number) {
    const c = this.categoryQuery.toLowerCase().trim();
    this.recipeCategoryService.getByName(this.categoryQuery).then(res => {
      const category = res;
      this.recipeCategoryService.subscribeToCategory(category.id).then(res => {
        this.categories.push(category);
        this.categoryQuery = '';
        this.fetchUnFollowedCategories();
      });
    });
  }

  private unSubscribeToCategory(index: number) {
    const category: RecipeCategory = this.categories[index];
    this.recipeCategoryService.unSubscribeToCategory(category.id).then(res => {
      this.categories.splice(index, 1);
      this.resultCategories.push(category);
    });
  }

  private fetchUnFollowedCategories() {
    this.recipeCategoryService.getUnFollowedCategories(this.route.snapshot.params['id']).subscribe((res : RecipeCategory[]) => {
        this.resultCategories= res;
    });
  }

  private static checkEmail(control: AbstractControl){
    if(control.value !== '') {
      control.setValidators([Validators.email, Validators.minLength(5)]);
    }
  }

  private editProfile(){
    const name = this.profileForm.value.name;
    if (name !== null && name.length !== 0) {
      this.user.name = this.formatter.capitalizeFirstChar(name);
    }
    const lastName = this.profileForm.value.lastName;
    if (lastName !== null && lastName.length !== 0) {
      this.user.lastName = this.formatter.capitalizeFirstChar(lastName);
    }
    const email = this.profileForm.value.email;
    if (email !== null && email.length !== 0) this.user.email = email;

    this.userService.modifyUser(this.user.id, this.user).then(() => {
      this.toaster.pop('success', 'Perfil Modificado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido modificar el perfil');
    });
    this.profileForm.reset();
    this.closeBtn.nativeElement.click();
  }

  private deleteUser() {
    this.userService.deleteUser(this.user.id).then(() => {
      this.myAuthService.logout();
    }, () => {
      this.toaster.pop('error', 'Usuario no eliminado');
    });
  }

  private fetchNews() {
    this.newsService.getUserNews(this.user.id).then(res => {
      this.news = res;
    });
  }

  private searchUsers() {
    if (this.usersQuery.length == 0) {
      this.resultUsers = [];
      return;
    }
    this.userService.searchUsers(this.usersQuery).then(res => {
      this.resultUsers = res;
    });
  }
}
