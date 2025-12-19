import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.html',
  styleUrls: ['./product-form.scss']
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;
  isEditMode = false;
  productId: number | null = null;
  shopId: number | null = null;

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
    this.shopId = Number(this.route.snapshot.paramMap.get('shopId'));
    const stateData = history.state.productData;

    if (stateData) {
      this.isEditMode = true;
      this.productId = stateData.id;
      this.productForm.patchValue(stateData);
    }
  }

  onSubmit() {
    if (this.productForm.invalid) return;
    const productPayload = this.productForm.value;
    if (this.isEditMode && this.productId) {
      this.productService.updateProduct(this.shopId!, this.productId, productPayload).subscribe({
        next: () => this.goBack(),
        error: (err) => alert('Errore durante l\'aggiornamento')
      });
    } else {
      this.productService.createProduct(this.shopId!, productPayload).subscribe({
        next: () => this.goBack(),
        error: (err) => alert('Errore durante la creazione')
      });
    }
  }

  goBack() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }
}