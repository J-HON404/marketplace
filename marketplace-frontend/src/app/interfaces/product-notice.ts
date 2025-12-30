// Enum dei possibili tipi di avvisi relativi ai prodotti.
export enum TypeNotice {
    AVAILABILITY = 'AVAILABILITY',
    STOCK='STOCK',
    PROMOTION='PROMOTION',
    WARNING='WARNING ',
    INFO='INFO'
}
// Rappresenta un avviso relativo a un prodotto.
export interface ProductNotices {
  id: number;
  text: string;
  expireDate?: string; 
  type:TypeNotice; 
  productId :number;
}