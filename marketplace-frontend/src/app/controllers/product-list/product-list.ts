import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../core/services/token.service';
import { Products } from '../../models/interfaces/product';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { ApiResponse } from '../../models/interfaces/api-response'; 

/**
 * ProductListComponent è un componente  che gestisce la visualizzazione
 * dei prodotti di uno shop specifico e le azioni che l'utente può compiere sui prodotti.
 * 
 * Funzioni principali:
 * 1. Carica i prodotti di uno shop specifico usando ProductService.
 * 2. Permette all'utente di aggiungere prodotti al carrello tramite CartService.
 * 3. Permette al proprietario dello shop (SELLER) di modificare o eliminare prodotti.
 * 4. Gestisce la navigazione verso la pagina form del prodotto, per editare o aggiungere un prodotto.
 */


@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: '../../views/product-list/product-list.html',
  styleUrls: ['../../views/product-list/product-list.scss']
})
export class ProductListComponent implements OnInit {
  profileId: number | null = null;
  shopId: number | null = null;
  userRole: string | null = null;
  products: Products[] = []; 
  loading = false;
  errorMessage = '';
  isOwner: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private productService: ProductService,
    public tokenService: TokenService,
    private cartService: CartService
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

    this.checkPermissions();
    this.loadProducts(this.shopId);
  }

  onAddToCart(product: Products) {
    if (!this.profileId || !this.shopId) {
      alert("Errore: Informazioni profilo o negozio mancanti.");
      return;
    }

    if (product.quantity <= 0) {
      alert("Prodotto esaurito!");
      return;
    }

    this.cartService.addProductToCart(
      this.profileId, 
      this.shopId, 
      product.id, 
      1 //quantità
    ).subscribe({
      next: (cart) => {
        alert(`${product.name} aggiunto al carrello!`);
        console.log('Stato carrello attuale:', cart);
      },
      error: (err) => {
        console.error('Errore aggiunta carrello:', err);
        const backendMessage = err.error?.message || 'Errore durante l\'aggiunta.';
        alert(backendMessage);
      }
    });
  }

  checkPermissions() {
    if (this.userRole === 'SELLER' && this.profileId && this.shopId) {
      this.productService.checkOwnership(this.profileId, this.shopId).subscribe({
        next: (res) => {
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
      next: (res: ApiResponse<Products[]>) => {
        const rawData = res.data || res;
        this.products = rawData.map((p: Products) => ({
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

  if (this.shopId == null) {
    alert('Errore: ID negozio mancante');
    return;
  }

  this.productService.deleteProduct(this.shopId, productId).subscribe({
    next: () => {
      this.products = this.products.filter(p => p.id !== productId);
    },
    error: (err) => {
      const errorMessage = err.error?.message || 'Errore durante l\'eliminazione.';
      alert(errorMessage);
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

onEditProduct(product: Products) {
  if (!product || !product.id) {
    console.error("Dati prodotto mancanti per la modifica");
    return;
  }
  this.router.navigate(
    ['/profiles', this.profileId, 'shops', this.shopId, 'products', 'editor', product.id],
    { 
      state: { productData: product } 
    }
  );
}

  onAddProduct() {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products', 'editor']);
  }

  onViewDetails(productId: number) {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products', productId, 'details']);
  }
}