import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { TokenService } from '../../services/token';
import { HttpErrorResponse } from '@angular/common/http';
import { Products } from '../../interfaces/products';
import { ProductService } from '../../services/product';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './product-list.html',
  styleUrls: ['./product-list.scss']
})
export class ProductListComponent implements OnInit {
  profileId: number | null = null;
  shopId: number | null = null;
  userRole: string | null = null;
  products: Products[] = []; 
  loading = false;
  errorMessage = '';

  constructor(
    private router:Router,
    private route: ActivatedRoute,
    private productService: ProductService,
    public tokenService: TokenService
  ) {}

  ngOnInit() {
    const params = this.route.snapshot.paramMap;
    this.profileId = Number(params.get('profileId')) || this.tokenService.getProfileId();
    this.shopId = Number(params.get('shopId'));
    this.userRole = this.tokenService.getUserRole();
    
    if (!this.profileId || !this.shopId) {
      this.errorMessage = 'Parametri mancanti: ID profilo o ID negozio non validi';
      return;
    }
    
    this.loadProducts(this.shopId);
  }

  loadProducts(shopId: number) {
    this.loading = true;
    this.productService.getProductInfo(shopId).subscribe({
      next: (res: any) => {
        const rawData = res.data || res;
        
        this.products = rawData.map((p: any) => ({
          id: p.id,
          name: p.name,
          description: p.description,
          price: p.price,
          quantity: p.quantity,
          availabilityDate: p.availabilityDate,
          shopId: p.shopId || shopId 
        }));

        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Errore caricamento prodotti', err);
        this.errorMessage = 'Si è verificato un errore nel caricamento dei prodotti.';
        this.loading = false;
      }
    });
  }

onDeleteProduct(productId: number) {
  if (!window.confirm('Eliminare definitivamente questo prodotto?')) return;
  this.productService.deleteProduct(this.shopId!, productId).subscribe({
    next: () => {
      this.products = this.products.filter(p => p.id !== productId);
    },
    error: (err) => {
      console.error('Errore:', err);
      alert('Errore durante l\'eliminazione.');
    }
  });
}

isFutureDate(dateString: string): boolean {
  if (!dateString) return false;
  const date = new Date(dateString);
  const today = new Date();
  date.setHours(0, 0, 0, 0);
  today.setHours(0, 0, 0, 0);
  return date > today;
}

// Metodo per andare al form in modalità MODIFICA (passando i dati)
onEditProduct(product: any) {
  this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products', 'editor', product.id],
    { state: { productData: product } }
  );
}

// Metodo per andare al form in modalità CREAZIONE (vuoto)
onAddProduct() {
  this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products', 'editor']);
}

}