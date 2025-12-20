import { Products } from "./products";


export interface CartItem {
    id: number;        
    product: Products; 
    cartId:number;
    quantity: number;   
}