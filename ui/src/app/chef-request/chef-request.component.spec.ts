import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChefRequestComponent } from './chef-request.component';

describe('ChefRequestComponent', () => {
  let component: ChefRequestComponent;
  let fixture: ComponentFixture<ChefRequestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChefRequestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChefRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
