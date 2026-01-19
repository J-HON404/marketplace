import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { NoticeService } from '../../services/product-notice.service'; 
import { TokenService } from '../../core/services/token.service';
import { NoticeHelperService } from '../../common/helpers/notice-helper.service';
import { ProductNotices, TypeNotice } from '../../models/interfaces/product-notice';
import { Products } from '../../models/interfaces/product';
import { ApiResponse } from '../../models/interfaces/api-response';

/**
 * ProductDetailsComponent è un componente standalone che mostra i dettagli di un singolo prodotto
 * insieme agli avvisi (ProductNotices) associati.
 * Funzioni principali:
 * 1. Controlla se l'utente loggato è il proprietario del prodotto (solo per SELLER).
 * 2. Carica i dati del prodotto dal backend tramite ProductService.
 * 3. Carica gli avvisi associati al prodotto tramite NoticeService.
 * 4. Permette ai SELLER di aggiungere nuovi avvisi/notifiche al prodotto.
 * 5. Permette ai SELLER di eliminare avvisi esistenti.
 */

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: '../../views/product-details/product-details.html',
  styleUrls: ['../../views/product-details/product-details.scss']
})
export class ProductDetailsComponent implements OnInit {
  product: Products | null = null;
  notices: ProductNotices[] = [];
  
  newNoticeContent: string = '';
  newNoticeExpireDate: string = '';
  minDate: string = new Date().toISOString().split('T')[0];
  
  selectedType: TypeNotice = TypeNotice.INFO;
  typeOptions = Object.values(TypeNotice);
  
  shopId: number | null = null;
  productId: number | null = null;
  profileId: number | null = null;
  isOwner: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private noticeService: NoticeService,
    public tokenService: TokenService,
    public noticeHelper: NoticeHelperService 
  ) {}

  ngOnInit() {
    const params = this.route.snapshot.paramMap;
   const productParam = params.get('productId');
   const shopParam = params.get('shopId');
   const profileParam = params.get('profileId');

    this.productId = productParam ? Number(params.get('productId')) : null;
    this.shopId = shopParam ? Number(params.get('shopId')) : null;
    this.profileId = profileParam ? Number(params.get('profileId')) : null;

  if (this.productId == null || this.shopId == null || this.profileId == null) {
    console.error('Parametri mancanti o invalidi');
    this.goBack();
    return;
  }

    this.checkPermissions();
    this.loadProductData();
    this.loadNotices();
  }

  checkPermissions() {
    if (this.tokenService.getUserRole() === 'SELLER') {
      this.productService.checkOwnership(this.profileId!, this.shopId!).subscribe({ //non può essere null
        next: (res: ApiResponse<boolean>) => this.isOwner = res.data === true,
        error: () => this.isOwner = false
      });
    }
  }

loadProductData() {
  this.productService.getProductById(this.shopId!, this.productId!).subscribe({ //non può essere null
    next: (res: ApiResponse<Products>) => this.product = res.data,
    error: () => this.goBack()
  });
}

  loadNotices() {
    this.noticeService.getProductNoticeOfProduct(this.productId!).subscribe({ //non può essere null
      next: (res: ApiResponse<ProductNotices[]>) => this.notices = res.data || [],
      error: () => this.notices = []
    });
  }

  onAddNotice() {
    if (!this.newNoticeContent.trim()) return;

    const noticeToSave = {
      text: this.newNoticeContent,
      type: this.selectedType,
      productId: this.productId,
      expireDate: this.newNoticeExpireDate || undefined
    } as ProductNotices;

    this.noticeService.createProductNotice(this.productId!, noticeToSave).subscribe({  //non può essere null
      next: () => {
        this.newNoticeContent = '';
        this.newNoticeExpireDate = '';
        this.loadNotices();
      }
    });
  }

  onDeleteNotice(noticeId: number) {
    if (!window.confirm('Eliminare definitivamente questo avviso?')) return;
    this.noticeService.deleteProductNotice(this.productId!, noticeId).subscribe({ //non può essere null
      next: () => {
        this.notices = this.notices.filter(n => n.id !== noticeId);
      }
    });
  }

  goBack() {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products']);
  }
}