import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { TokenService } from '../../services/token';
import { HttpErrorResponse } from '@angular/common/http';
import { Products } from '../../interfaces/products';
import { ProductService } from '../../services/product';

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
  isOwner: boolean = false; // Sarà impostata dal backend

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private productService: ProductService,
    public tokenService: TokenService
  ) {}

  ngOnInit() {
    const params = this.route.snapshot.paramMap;
    this.profileId = Number(params.get('profileId'));
    this.shopId = Number(params.get('shopId'));
    this.userRole = this.tokenService.getUserRole();
    
    if (!this.profileId || !this.shopId) {
      this.errorMessage = 'Parametri mancanti: ID profilo o ID negozio non validi';
      return;
    }

    // 1. Verifichiamo i permessi dal Backend
    this.checkPermissions();
    
    // 2. Carichiamo i prodotti
    this.loadProducts(this.shopId);
  }

  checkPermissions() {
    if (this.userRole === 'SELLER' && this.profileId && this.shopId) {
      this.productService.checkOwnership(this.profileId, this.shopId).subscribe({
        next: (res) => {
          // Usiamo la risposta del server (ApiResponse.data)
          this.isOwner = res.data === true;
        },
        error: () => {
          this.isOwner = false;
        }
      });
    }
  }

  loadProducts(shopId: number) {
    this.loading = true;
    this.productService.getProducts(shopId).subscribe({
      next: (res: any) => {
        const rawData = res.data || res;
        this.products = rawData.map((p: any) => ({
          ...p,
          shopId: p.shopId || shopId 
        }));
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
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
      error: () => alert('Errore durante l\'eliminazione.')
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

  onEditProduct(product: any) {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products', 'editor', product.id]);
  }

  onAddProduct() {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products', 'editor']);
  }

  onViewDetails(productId: number) {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products', productId, 'details']);
  }
}