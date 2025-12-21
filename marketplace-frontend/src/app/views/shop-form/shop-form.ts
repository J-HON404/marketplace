import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ShopService } from '../../services/shop.service';
import { ShopCategory } from '../../interfaces/shops';

@Component({
  selector: 'app-shop-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], 
  templateUrl: './shop-form.html',
  styleUrl: './shop-form.scss',
})
export class ShopFormComponent implements OnInit {
  shopForm: FormGroup;
  isEditMode = false;
  profileId!: number;
  shopId?: number;
  categories = Object.values(ShopCategory);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private shopService: ShopService
  ) {
    this.shopForm = this.fb.group({
      // Usiamo 'category' per combaciare con updatedShop.getCategory() in Java
      name: ['', [Validators.required, Validators.minLength(3)]],
      category: ['', Validators.required] 
    });
  }

  ngOnInit() {
    this.profileId = Number(this.route.snapshot.paramMap.get('profileId'));
    const id = this.route.snapshot.paramMap.get('shopId');
    
    if (id) {
      this.isEditMode = true;
      this.shopId = Number(id);
      
      this.shopService.getShop(this.profileId).subscribe({
        next: (res) => {
          if (res.data) {
            // MAPPATURA: res.data.shopCategory (dal server) -> category (nel form)
            this.shopForm.patchValue({
              name: res.data.name,
              category: res.data.shopCategory
            });
          }
        },
        error: () => {
          // Se 404, rimaniamo in modalità creazione
          this.isEditMode = false;
        }
      });
    }
  }

  goBack() {
    this.router.navigate(['/home']);
  }

  onSubmit() {
    if (this.shopForm.invalid) return;
    
    // Ora il payload conterrà { name: "...", category: "..." }
    const request = this.isEditMode 
      ? this.shopService.updateShop(this.profileId, this.shopId!, this.shopForm.value)
      : this.shopService.createShop(this.profileId, this.shopForm.value);
    
    request.subscribe({
      next: (res) => {
        alert(res.message || "Operazione completata!");
        this.goBack();
      },
      error: (err) => alert(err.error?.message || "Errore durante l'operazione")
    });
  }

}