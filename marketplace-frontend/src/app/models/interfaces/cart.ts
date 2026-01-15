import { CartItem } from './cart-item';

// Rappresenta un carrello dell'utente contenente più prodotti.
export interface Cart {
    id: number;
    shopId: number;
    profileId:number;
    items: CartItem[];   
    totalPrice?: number;
}