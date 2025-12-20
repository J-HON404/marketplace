import { Products } from "./product";


export interface CartItem {
    id: number;        
    product: Products; 
    cartId:number;
    quantity: number;   
}