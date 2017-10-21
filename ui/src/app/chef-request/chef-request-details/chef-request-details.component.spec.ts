import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChefRequestDetailsComponent } from './chef-request-details.component';

describe('ChefRequestDetailsComponent', () => {
  let component: ChefRequestDetailsComponent;
  let fixture: ComponentFixture<ChefRequestDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChefRequestDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChefRequestDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
