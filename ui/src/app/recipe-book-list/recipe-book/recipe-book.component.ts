import {ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {RecipeBookService} from "../../shared/services/recipebook.service";
import {RecipeBook} from "../../shared/models/recipe/recipebook";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.component.html',
  styleUrls: ['./recipe-book.component.css']
})
export class RecipeBookComponent implements OnInit {

  recipeBookId: string;
  recipeBook: RecipeBook = new RecipeBook();
  @ViewChild('closeBtn') closeBtn: ElementRef;

  private recipeBookForm: FormGroup;

  constructor(private route: ActivatedRoute,
              private recipeBookService: RecipeBookService,
              public toaster: ToasterService,
              private router: Router,
              private fb: FormBuilder) {

    this.recipeBookForm = fb.group({
      'recipeBookName': new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          this.recipeBookId = params['id'];
        }
      );

    this.recipeBookService.get(this.recipeBookId).then(recipeBook => {
      this.recipeBook = recipeBook;
    });
  }

  modifyRecipeBook(){
    this.recipeBook.name = this.recipeBookForm.value.recipeBookName;
    this.recipeBookService.update(this.recipeBookId, this.recipeBook).then(() => {
      this.toaster.pop('success', 'Recetario Modificado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido modificar el recetario');
    });

    this.recipeBookForm.reset();
    this.closeBtn.nativeElement.click();
  }

  backToRecipeBooks(){
    this.router.navigate(['/recetarios']);
  }
}

