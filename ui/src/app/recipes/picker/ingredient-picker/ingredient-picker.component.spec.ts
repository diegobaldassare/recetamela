import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientPickerComponent } from './ingredient-picker.component';

describe('IngredientPickerComponent', () => {
  let component: IngredientPickerComponent;
  let fixture: ComponentFixture<IngredientPickerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IngredientPickerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IngredientPickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
