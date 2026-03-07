import { Products } from "./product";

// Rappresenta un singolo prodotto nel carrello.

export interface CartItem {
    id: number;        
    product: Products; 
    cartId:number;
    quantity: number;   
}