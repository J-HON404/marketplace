// Rappresenta un prodotto disponibile nel marketplace.
export interface Products {
  id: number;
  name: string;
  description: string; 
  price: number; 
  quantity:number;
  shopId :number;
  availabilityDate:string;
}