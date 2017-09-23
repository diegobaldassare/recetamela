import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddToRecipeBookComponent } from './add-to-recipe-book.component';

describe('AddToRecipeBookComponent', () => {
  let component: AddToRecipeBookComponent;
  let fixture: ComponentFixture<AddToRecipeBookComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddToRecipeBookComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddToRecipeBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
