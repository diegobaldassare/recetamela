import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {RecipeService} from "../shared/services/recipe.service";
import {Recipe} from "../shared/models/recipe/recipe";
import {RecipeCategory} from "../shared/models/recipe/recipe-category";
import {RecipeCategoryService} from "../shared/services/recipecategory.service";
import {FormatService} from "../shared/services/format.service";
import {
  AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn,
  Validators
} from "@angular/forms";
import {ToasterService} from "angular2-toaster";


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {


  private user: User;
  private loggedUser: User;
  public fetched: boolean;
  recipes: Recipe[] = [];
  followers: User[] = [];
  following: User[] = [];
  subscribed: boolean;
  categories: RecipeCategory[] = [];
  resultCategories: any[] = [];
  unFollowedCategories: RecipeCategory[] = [];
  private categoryQuery: string = "";
  private profileForm: FormGroup;
  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private recipeService: RecipeService,
    private recipeCategoryService: RecipeCategoryService,
    private router: Router,
    public toaster: ToasterService,
    private formBuilder: FormBuilder) {

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
      'email': [''],
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
          this.fetched = true;
          this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
          this.fetchRecipes();
          this.fetchFollowers();
          this.fetchFollowing();
          this.fetchCategories();
          this.fetchUnFollowedCategories();
        }, () => { this.fetched = true });
      }
    );

    this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
  }

  private subscribe() {
    this.userService.followUser(this.user.id).subscribe((res : User) => {
      this.followers.push(res);
      this.subscribed = true;
    });
  }

  private unSubscribe() {
    this.userService.unFollowUser(this.user.id).subscribe((res : User) => {
      const index = this.followers.map(u => u.id).indexOf(res.id, 0);
      if (index > -1) {
        this.followers.splice(index, 1);
        this.subscribed = false;
      }
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

  private fetchCategories() {
    this.userService.getCategories(this.route.snapshot.params['id']).subscribe((res : RecipeCategory[]) => {
      this.categories = res;
    });
  }

  private search() {
    if (this.categoryQuery.length == 0) return;
    this.recipeCategoryService.searchCategories(this.categoryQuery).then(res => {
      this.resultCategories = res;
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
    });
  }

  private fetchUnFollowedCategories() {
    this.userService.getUnfollowedCategories(this.route.snapshot.params['id']).subscribe((res : RecipeCategory[]) => {
        this.unFollowedCategories= res;
    });
  }

  private editProfile(){
    this.user.name = this.profileForm.value.name;
    this.user.lastName = this.profileForm.value.lastName;
    if (this.profileForm.value.email !== null) this.user.email = this.profileForm.value.email;
    this.userService.modifyUser(this.user.id, this.user).then(() => {
      this.toaster.pop('success', 'Perfil Modificado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido modificar el perfil');
    });

    this.profileForm.reset();
    this.closeBtn.nativeElement.click();
  }

  private deleteAccount() {
    this.userService.deleteUser(this.user.id).then(() => {
      this.router.navigate(['/']);
    }, () => {
      this.toaster.pop('error', 'Usuario no eliminado');
    });
  }
}
