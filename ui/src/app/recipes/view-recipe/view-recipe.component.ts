import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {DomSanitizer} from '@angular/platform-browser';
import {Recipe} from "../../shared/models/recipe/recipe";
import {RecipeService} from "../../shared/services/recipe.service";
import {User} from "../../shared/models/user-model";
import {SharedService} from "../../shared/services/shared.service";
import {FormatService} from "../../shared/services/format.service";
import {OnClickEvent} from "angular-star-rating";
import {RecipeRating} from "../../shared/models/recipe/recipe-rating";
import {ToasterService} from "angular2-toaster";
import {RecipeCommentary} from "../../shared/models/recipe/recipe-comment";
import {RecipeCommentaryService} from "../../shared/services/comment.service";

@Component({
  selector: 'app-view-recipe',
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.scss']
})
export class ViewRecipeComponent implements OnInit {

  recipe: Recipe;
  public fetched: boolean;
  private viewer: User = JSON.parse(localStorage.getItem("user"));
  commentaries: RecipeCommentary[];
  private recipeRating: RecipeRating = new RecipeRating();
  private chefLiked: boolean;
  private likesByChef: User[] = [];

  constructor(
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private recipeService: RecipeService,
    private sharedService: SharedService,
    private router: Router,
    private formatter: FormatService,
    private recipeCommentaryService: RecipeCommentaryService,
    public toaster: ToasterService
  ){ }

  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          const id = this.route.snapshot.params['id'];
          this.getLikesByChef(id);
          this.recipeService.getRecipe(id).then(recipe => {
            this.recipe = recipe;
            this.fetched = true;

            this.recipeService.getComments(this.recipe.id).then(res => {
              this.commentaries = res;
            });
            this.hasChefLiked();
          }, () => { this.fetched = true });
          this.recipeService.getRatingFromUser(id).then( recipeRating => {
              this.recipeRating = recipeRating;
            }
          )
        }
      );
  }

  private hasChefLiked() {
    for(let i = 0; i < this.likesByChef.length; i++) {
      if(this.likesByChef[i].id == this.viewer.id) {
          this.chefLiked = true;
      }
    }
  }

  private getLikesByChef(id: string) {
    this.recipeService.getRecipeLikesByChef(id).then(likes  =>
        this.likesByChef = likes
    )
  }

  public get editButton(): boolean {
    return this.viewer.id == this.recipe.author.id;
  }

  private checkPremium() {
    const u : User = JSON.parse(localStorage.getItem("user")) as User;
    if (u.type == 'FreeUser') {
      this.sharedService.notifyOther({upgradeForm:true});
    }
    else {
      const id = this.route.snapshot.params['id'];
      this.router.navigate(['/recetas/' + id + '/editar']);
    }
  }

  public get canAddToRecipeBook(): boolean{
    const u : User = JSON.parse(localStorage.getItem("user")) as User;
    return (u.type != 'FreeUser');
  }

  private get embedVideoUrl() {
    const split = this.recipe.videoUrl.split('v=');
    const url = `https://www.youtube.com/embed/${split[1]}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  private addRating($event:OnClickEvent) {
    const rating = new RecipeRating();
    rating.rating = $event.rating;
    this.recipeService.addRating(this.recipe.id, rating).then(recipe =>
      this.recipe = recipe
    )
  }

  publishComment(textComment: string){
      if(textComment!='') {
        let commentary = new RecipeCommentary();
        commentary.comment = textComment;

        this.recipeCommentaryService.createRecipeCommentary(commentary, this.recipe.id).then((res) => {
          this.toaster.pop('success', 'Comentado');

          this.commentaries.unshift(res);


        }, () => {
          this.toaster.pop('error', 'No se ha podido comentar');
        });

      }
      else{
        this.toaster.pop('error', 'Ingresa un comentario');
      }
  }

  deleteComment(id: string){
    let index;
    for (let i=0; i<this.commentaries.length; i++){
      if(this.commentaries[i].id == id){
        index = i;
      }
    }
    if (index > -1) {
      this.commentaries.splice(index, 1);
    }

    this.recipeCommentaryService.deleteRecipeCommentary(id).then(() => {
      this.toaster.pop('success', 'Eliminado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido eliminar');
    });

  }


  private get chefCanRate(): boolean {
      return (this.viewer.type == 'ChefUser' && this.viewer.id != this.recipe.author.id);
  }

  private likeRecipe() {
    this.recipeService.addLikeByChef(this.recipe.id, this.recipe).then(recipe => {
        this.recipe = recipe;
        this.chefLiked = true;
        this.likesByChef.push(this.viewer);
      }
    )
  }

  private dislikeRecipe() {
      this.recipeService.deleteLikeByChef(this.recipe.id).then(recipe => {
        this.recipe = recipe;
        this.chefLiked = false;
        this.deleteChefLike();
      });
  }

  private deleteChefLike(){
    const likes: User[] = [];
    for(let i = 0; i < this.likesByChef.length; i++) {
      if(this.likesByChef[i].id != this.viewer.id) {
        likes.push(this.likesByChef[i]);
      }
    }
    this.likesByChef = likes;
  }
}
