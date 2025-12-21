import { Routes } from '@angular/router';
import { LoginComponent } from './views/login/login';
import { RegisterComponent } from './views/register/register';
import { HomeComponent } from './views/home/home'; 
import { AreaRiservataComponent } from './views/area-riservata/area-riservata';
import { OrdersPageComponent } from './views/orders-page/orders-page'; 
import { ShopListComponent } from './views/shop-list/shop-list';
import { ProductListComponent } from './views/product-list/product-list';
import { ProductFormComponent } from './views/product-form/product-form';
import { ProductDetailsComponent } from './views/product-details/product-details';
import { CartComponent } from './views/cart/cart';
import { ShopFormComponent } from './views/shop-form/shop-form';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent }, 
  { path: 'profile/:id', component: AreaRiservataComponent },
  { path: 'profiles/:profileId/orders', component: OrdersPageComponent },
  { path: 'profiles/:profileId/shops', component: ShopListComponent },
  { path: 'profiles/:profileId/shop/editor', component: ShopFormComponent },
  { path: 'profiles/:profileId/shop/editor/:shopId', component: ShopFormComponent },
  { path: 'profiles/:profileId/shops/:shopId/products', component: ProductListComponent },
  { path: 'profiles/:profileId/shops/:shopId/products/editor', component: ProductFormComponent },
  { path: 'profiles/:profileId/shops/:shopId/products/editor/:productId', component: ProductFormComponent },
  { path: 'profiles/:profileId/shops/:shopId/products/:productId/details', component: ProductDetailsComponent },
  { path: 'profiles/:profileId/shops/:shopId/cart', component: CartComponent },
  { path: '**', redirectTo: 'login' }
];