import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { HomeComponent } from './components/home/home'; 
import { AreaRiservataComponent } from './components/area-riservata/area-riservata';
import { OrdersPageComponent } from './components/orders-page/orders-page'; 
import { ShopListComponent } from './components/shop-list/shop-list';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent }, 
  { path: 'profile/:id', component: AreaRiservataComponent },
  { path: 'profiles/:id/orders', component: OrdersPageComponent },
  { path: 'profiles/:id/shops', component: ShopListComponent },
  { path: '**', redirectTo: 'login' }
];
