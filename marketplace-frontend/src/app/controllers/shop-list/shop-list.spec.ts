import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShopListComponent } from './shop-list';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

describe('ShopListComponent', () => { // Nome aggiornato per coerenza
  let component: ShopListComponent;
  let fixture: ComponentFixture<ShopListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShopListComponent],
      providers: [
        provideHttpClient(),        
        provideHttpClientTesting(), 
        provideRouter([])        
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShopListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});