import { Routes } from '@angular/router';
import { LoginComponent } from './controllers/login/login';
import { RegisterComponent } from './controllers/register/register';
import { HomeComponent } from './controllers/home/home'; 
import { AreaRiservataComponent } from './controllers/area-riservata/area-riservata';
import { ShopListComponent } from './controllers/shop-list/shop-list';
import { ProductListComponent } from './controllers/product-list/product-list';
import { ProductFormComponent } from './controllers/product-form/product-form';
import { ProductDetailsComponent } from './controllers/product-details/product-details';
import { CartComponent } from './controllers/cart/cart';
import { ShopFormComponent } from './controllers/shop-form/shop-form';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent }, 
  { path: 'profile/:id', component: AreaRiservataComponent },
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