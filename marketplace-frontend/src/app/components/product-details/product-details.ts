import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product';
import { NoticeService } from '../../services/productNotice'; 
import { TokenService } from '../../services/token';
import { ProductNotices, TypeNotice } from '../../interfaces/productNotices';
import { Products } from '../../interfaces/products';

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
    public tokenService: TokenService 
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
        next: (res) => {
          // res.data contiene il boolean restituito dalla tua ApiResponse
          this.isOwner = res.data === true;
        },
        error: () => this.isOwner = false
      });
    }
  }

  loadProductData() {
    this.productService.getProductById(this.shopId, this.productId).subscribe({
      next: (res: any) => {
        this.product = res.data || res;
      },
      error: () => this.goBack()
    });
  }

  loadNotices() {
    this.noticeService.getProductNoticeOfProduct(this.productId).subscribe({
      next: (n) => this.notices = n || []
    });
  }

  onAddNotice() {
    if (!this.newNoticeContent.trim()) return;
    const noticeToSave = {
      text: this.newNoticeContent,
      type: this.selectedType,
      productId: this.productId
    } as ProductNotices;

    this.noticeService.createProductNotice(this.productId, noticeToSave).subscribe({
      next: () => {
        this.newNoticeContent = '';
        this.loadNotices();
      }
    });
  }

  onDeleteNotice(noticeId: number) {
    if (!window.confirm('Eliminare definitivamente questo avviso?')) return;
    this.noticeService.deleteProductNotice(this.productId, noticeId).subscribe({
      next: () => this.notices = this.notices.filter(n => n.id !== noticeId)
    });
  }

  goBack() {
    this.router.navigate(['/profiles', this.profileId, 'shops', this.shopId, 'products']);
  }
}