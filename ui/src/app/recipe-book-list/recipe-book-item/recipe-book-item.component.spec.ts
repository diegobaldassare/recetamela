import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeBookItemComponent } from './recipe-book-item.component';

describe('RecipeBookItemComponent', () => {
  let component: RecipeBookItemComponent;
  let fixture: ComponentFixture<RecipeBookItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecipeBookItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecipeBookItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
