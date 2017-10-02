import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {RecipeCategoryService} from "../../shared/services/recipecategory.service";
import {ToasterService} from "angular2-toaster";
import {RecipeCategory} from "../../shared/models/recipe/recipe-category";

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {

  private createCategoryForm: FormGroup;
  @ViewChild('closeBtn') closeBtn: ElementRef;
  categories: RecipeCategory[] = [];

  constructor(private fb: FormBuilder,
              public toaster: ToasterService,
              private categoryService: RecipeCategoryService) {
    this.createCategoryForm = fb.group({
    'categoryName': new FormControl(null, [Validators.required]),
  });}

  ngOnInit() {
    // this.categoryService.getAll().then(categories => {
    //   this.categories = categories;
    // })
  }


  createCategory(){
    let category = new RecipeCategory();
    category.name = this.createCategoryForm.value.categoryName;
    this.categoryService.create(category).then(() => {
      this.toaster.pop('success', 'Categoria Creada');
    }, () => {
      this.toaster.pop('error', 'No se ha podido crear la categoria');
    });

    this.createCategoryForm.reset();
    this.closeBtn.nativeElement.click();
  }
}
