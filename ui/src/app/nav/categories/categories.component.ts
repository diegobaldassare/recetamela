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
  private modifyCategoryForm: FormGroup;

  @ViewChild('closeBtn') closeBtn: ElementRef;
  categories: RecipeCategory[] = [];

  constructor(private fb: FormBuilder,
              public toaster: ToasterService,
              private categoryService: RecipeCategoryService) {

    this.createCategoryForm = fb.group({
      'categoryName': new FormControl(null, [Validators.required]),
    });

    this.modifyCategoryForm = fb.group({
      'newCategoryName': new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit() {
    this.categoryService.getAll().then(categories => {
      this.categories = categories;
    })
  }


  createCategory() {
    let category = new RecipeCategory();
    category.name = this.createCategoryForm.value.categoryName.toLowerCase();
    this.categoryService.create(category).then((res) => {
      this.toaster.pop('success', 'Categoria Creada');
      this.categories.push(res);
    }, () => {
      this.toaster.pop('error', 'No se ha podido crear la categoria');
    });

    this.createCategoryForm.reset();
  }

  editCategory(categoryId: string) {
    document.getElementById(categoryId + "-edit").style.display = "none";
    document.getElementById(categoryId + "-ok").style.display = "inline";
    document.getElementById(categoryId + "-name").style.display = "none";
    document.getElementById(categoryId + "-input").style.display = "inline";
  }

  modifyCategory(categoryId: string) {
    document.getElementById(categoryId + "-edit").style.display = "inline";
    document.getElementById(categoryId + "-ok").style.display = "none";
    document.getElementById(categoryId + "-name").style.display = "inline";
    document.getElementById(categoryId + "-input").style.display = "none";
    document.getElementById(categoryId+"-name").innerHTML = this.modifyCategoryForm.value.newCategoryName;

    let category = new RecipeCategory();
    category.name = (this.modifyCategoryForm.value.newCategoryName);
    category.name = category.name.toLowerCase();
    this.categoryService.update(categoryId, category).then(() => {
      this.toaster.pop('success', 'Categoria Modificada');
    }, () => {
      this.toaster.pop('error', 'No se ha podido modificar la categoria');
    });
    this.modifyCategoryForm.reset();
  }


  deleteCategory(categoryId: string) {

    this.categoryService.get(categoryId).then((res) => {

      let index;
      for (let i=0; i<this.categories.length; i++){
        if(this.categories[i].id == res.id){
          index = i;
        }
      }

      this.categoryService.delete(categoryId).then(() => {
        this.toaster.pop('success', 'Categoria Eliminada');

        if (index > -1) {
          this.categories.splice(index, 1);
        }

        }, () => {
        this.toaster.pop('error', 'No se ha podido eliminar la categoria');
      });

    }, ()=>{
      this.toaster.pop('error', 'No se ha podido encontrar la categoria');
    })
  }
}



