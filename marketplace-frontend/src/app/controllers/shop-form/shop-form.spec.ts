import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopForm } from './shop-form';

describe('ShopForm', () => {
  let component: ShopForm;
  let fixture: ComponentFixture<ShopForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShopForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShopForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
