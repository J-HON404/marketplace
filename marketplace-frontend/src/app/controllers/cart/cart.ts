import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../services/cart.service';
import { Cart } from '../../models/interfaces/cart';

/**
 * CartComponent è un componente che gestisce il carrello dell'utente.
 * Funzionalità principali:
 * - Recupera l'ID del profilo e del negozio dall'URL tramite ActivatedRoute.
 * - Carica il carrello associato a un profilo e a uno shop tramite CartService.
 * - Consente di aggiornare la quantità dei prodotti nel carrello.
 * - Permette di rimuovere singoli prodotti o svuotare completamente il carrello.
 * - Gestisce il checkout del carrello e reindirizza l'utente alla pagina del profilo dopo l'ordine.
 * - Calcola il totale del carrello sommando prezzo e quantità di ciascun prodotto.
 */

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: '../../views/cart/cart.html',
  styleUrls: ['../../views/cart/cart.scss']
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
      if (this.profileId && this.shopId) {
        this.loadCart();
      }
    });
  }

  loadCart() {
    this.loading = true;
    this.cartService.getCart(this.profileId, this.shopId).subscribe({
      next: (res) => {
        this.cart = res.data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Errore nel caricamento del carrello.';
        this.loading = false;
      }
    });
  }

  onUpdateQuantity(productId: number, newQuantity: number) {
  if (newQuantity < 1) {
    this.onRemoveProduct(productId);
    return;
  }
  this.cartService.updateProductQuantity(this.profileId, this.shopId, productId, newQuantity)
    .subscribe({
      next: (res) => {
        this.cart = res.data;
      },
      error: (err) => {
        console.error('Errore aggiornamento quantità:', err);
        const message = err?.error?.message || 'Si è verificato un errore durante l\'aggiornamento del carrello.';
        alert(message);
      }
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
      next: (res) => {
        alert(res.message || 'Ordine effettuato con successo!');
        this.router.navigate(['/profile', this.profileId]); 
      },
      error: (err) => {
        const msg = err.error?.message || 'Impossibile completare l\'ordine.';
        alert('Errore: ' + msg);
      }
    });
  }

getTotal(): number {
  return this.cart?.items.reduce(
    (acc, item) => acc + item.product.price * item.quantity,0) ?? 0;
}

}