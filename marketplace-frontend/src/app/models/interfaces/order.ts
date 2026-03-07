
import { Profile } from './profile';
import { Shop } from './shops';

// Rappresenta un ordine nel marketplace.
export interface Order {
  id: number;
  date: string;
  total: number;
  status: string;
  trackingId?: string;             
  estimatedDeliveryDate?: string | null; 
 expired?: boolean;

    // campi richiesti dal template
  customer?: Profile;
  shop?: Shop;
  orderDate?: string; 

}