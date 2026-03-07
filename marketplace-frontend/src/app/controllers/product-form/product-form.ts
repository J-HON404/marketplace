import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';

/**
 * ProductFormComponent è un componente che gestisce il form per creare o modificare un prodotto.
 * Funzioni principali:
 * 1.  Permette di inserire un nuovo prodotto nel negozio specificato.
 * 2. Carica i dati del prodotto esistente e permette di aggiornarli.
 */

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: '../../views/product-form/product-form.html',
  styleUrls: ['../../views/product-form/product-form.scss']
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;
  isEditMode = false;
  productId: number | null = null;
  shopId: number | null = null;
  todayString: string = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0.01)]],
      quantity: [0, [Validators.required, Validators.min(0)]],
      availabilityDate: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.todayString = new Date().toISOString().split('T')[0];
    
    this.shopId = Number(this.route.snapshot.paramMap.get('shopId'));
    const productIdParam = this.route.snapshot.paramMap.get('productId');

    if (productIdParam) {
      this.isEditMode = true;
      this.productId = Number(productIdParam);
      
      const stateData = history.state.productData;
      if (stateData) {
        this.productForm.patchValue(stateData);
        this.productForm.get('name')?.disable();
      } else {
        this.productService.getProductById(this.shopId, this.productId).subscribe({
          next: (prod) => {
            this.productForm.patchValue(prod);
            this.productForm.get('name')?.disable();
          }
        });
      }
    }
  }

  onSubmit() {
    if (this.productForm.invalid) return;

    const productPayload = this.productForm.getRawValue();

    if (this.isEditMode && this.productId) {
      if (!this.shopId) {
        return;
      }
      this.productService.updateProduct(this.shopId, this.productId, productPayload).subscribe({
        next: () => this.goBack(),
        error: (err) => alert(err.error?.message || 'Errore durante l\'aggiornamento')
      });
    } else {
      this.productService.createProduct(this.shopId!, productPayload).subscribe({
        next: () => this.goBack(),
        error: (err) => alert(err.error?.message || 'Errore durante la creazione')
      });
    }
  }

goBack() {
  const profileId = this.route.snapshot.paramMap.get('profileId');
  const shopId = this.route.snapshot.paramMap.get('shopId');
  this.router.navigate(['/profiles', profileId, 'shops', shopId, 'products']);
}
}