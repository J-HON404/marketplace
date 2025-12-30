import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ShopService } from '../../services/shop.service';
import { ShopCategory } from '../../interfaces/shops';

/**
 * ShopFormComponent è un componente che gestisce la creazione e la modifica di un negozio.
 * Funzioni principali:
 * 1. Gestisce un form reattivo per l’inserimento dei dati del negozio (nome e categoria).
 * 2. Determina se è in modalità "creazione" o "modifica" basandosi sulla presenza di un shopId nella route.
 * 3. Se in modalità modifica, carica i dati del negozio esistente e popola il form.
 * 4. Invia i dati al backend tramite ShopService per creare o aggiornare il negozio.
 */

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
            this.shopForm.patchValue({
              name: res.data.name,
              category: res.data.shopCategory
            });
          }
        },
        error: () => {
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