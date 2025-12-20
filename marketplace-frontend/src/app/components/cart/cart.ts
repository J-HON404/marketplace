import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../services/cart';
import { Cart } from '../../interfaces/cart';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './cart.html',
  styleUrls: ['./cart.scss']
})
export class CartComponent implements OnInit {
  profileId!: number;
  shopId!: number;
  cart: Cart | null = null;
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.profileId = Number(params.get('profileId'));
      this.shopId = Number(params.get('shopId'));
      this.loadCart();
    });
  }

  loadCart() {
    this.loading = true;
    this.cartService.getCart(this.profileId, this.shopId).subscribe({
      next: (res: any) => {
        this.cart = res.data || res;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Errore nel caricamento del carrello.';
        this.loading = false;
      }
    });
  }

  onUpdateQuantity(productId: number, newQuantity: number) {
    if (!newQuantity || newQuantity < 1) {
      this.loadCart();
      return;
    }
    this.cartService.updateProductQuantity(this.profileId, this.shopId, productId, newQuantity).subscribe({
      next: (updatedCart: any) => this.cart = updatedCart.data || updatedCart
    });
  }

  onRemoveProduct(productId: number) {
    if (!confirm('Rimuovere questo prodotto dal carrello?')) return;
    this.cartService.removeProductFromCart(this.profileId, this.shopId, productId).subscribe({
      next: () => this.loadCart()
    });
  }

  onClearCart() {
    if (!confirm('Svuotare tutto il carrello?')) return;
    this.cartService.clearCart(this.profileId, this.shopId).subscribe({
      next: () => this.cart = null
    });
  }

  onCheckout() {
    if (!confirm('Confermi l\'ordine?')) return;
    this.cartService.checkout(this.profileId, this.shopId).subscribe({
      next: () => {
        alert('Ordine effettuato con successo!');
        this.router.navigate(['/profile', this.profileId]); 
      },
      error: (err) => alert('Errore: ' + (err.error?.message || 'Impossibile completare l\'ordine.'))
    });
  }

  getTotal(): number {
    if (!this.cart || !this.cart.items) return 0;
    return this.cart.items.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);
  }
}