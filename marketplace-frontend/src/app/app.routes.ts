import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { HomeComponent } from './components/home/home'; 
import { AreaRiservataComponent } from './components/area-riservata/area-riservata';
import { OrdersPageComponent } from './components/orders-page/orders-page'; 
import { ShopListComponent } from './components/shop-list/shop-list';
import { ProductListComponent } from './components/product-list/product-list';
import { ProductFormComponent } from './components/product-form/product-form';
import { ProductDetailsComponent } from './components/product-details/product-details';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent }, 
  { path: 'profile/:id', component: AreaRiservataComponent },
  { path: 'profiles/:profileId/orders', component: OrdersPageComponent },
  { path: 'profiles/:profileId/shops', component: ShopListComponent },
  { path: 'profiles/:profileId/shops/:shopId/products', component: ProductListComponent },
  { path: 'profiles/:profileId/shops/:shopId/products/editor', component: ProductFormComponent },
  { path: 'profiles/:profileId/shops/:shopId/products/editor/:productId', component: ProductFormComponent },
  { path: 'profiles/:profileId/shops/:shopId/products/:productId/details', component: ProductDetailsComponent },
  { path: '**', redirectTo: 'login' }
];