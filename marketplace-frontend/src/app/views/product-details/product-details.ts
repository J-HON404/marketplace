import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { NoticeService } from '../../services/product-notice.service'; 
import { TokenService } from '../../core/services/token.service';
import { NoticeHelperService } from '../../helpers/notice-helper.service';
import { ProductNotices, TypeNotice } from '../../interfaces/product-notice';
import { Products } from '../../interfaces/product';
import { ApiResponse } from '../../interfaces/api-response';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './product-details.html',
  styleUrls: ['./product-details.scss']
})
export class ProductDetailsComponent implements OnInit {
  product: Products | null = null;
  notices: ProductNotices[] = [];
  
  newNoticeContent: string = '';
  newNoticeExpireDate: string = '';
  minDate: string = new Date().toISOString().split('T')[0];
  
  selectedType: TypeNotice = TypeNotice.INFO;
  typeOptions = Object.values(TypeNotice);
  
  productId!: number;
  shopId!: number;
  profileId!: number;
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
    this.productId = Number(params.get('productId'));
    this.shopId = Number(params.get('shopId'));
    this.profileId = Number(params.get('profileId'));

    this.checkPermissions();
    this.loadProductData();
    this.loadNotices();
  }

  checkPermissions() {
    if (this.tokenService.getUserRole() === 'SELLER') {
      this.productService.checkOwnership(this.profileId, this.shopId).subscribe({
        next: (res: ApiResponse<boolean>) => this.isOwner = res.data === true,
        error: () => this.isOwner = false
      });
    }
  }

  loadProductData() {
    this.productService.getProductById(this.shopId, this.productId).subscribe({
      next: (res: ApiResponse<Products>) => this.product = res.data,
      error: () => this.goBack()
    });
  }

  loadNotices() {
    this.noticeService.getProductNoticeOfProduct(this.productId).subscribe({
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

    this.noticeService.createProductNotice(this.productId, noticeToSave).subscribe({
      next: () => {
        this.newNoticeContent = '';
        this.newNoticeExpireDate = '';
        this.loadNotices();
      }
    });
  }

  onDeleteNotice(noticeId: number) {
    if (!window.confirm('Eliminare definitivamente questo avviso?')) return;
    this.noticeService.deleteProductNotice(this.productId, noticeId).subscribe({
      next: () => {
        this.notices = this.notices.filter(n => n.id !== noticeId);
      }
    });
  }

  goBack() {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products']);
  }
}