export enum TypeNotice {
    AVAILABILITY = 'AVAILABILITY',
    STOCK='STOCK',
    PROMOTION='PROMOTION',
    WARNING='WARNING ',
    INFO='INFO'
}

export interface ProductNotices {
  id: number;
  text: string;
  expireDate?: string; 
  type:TypeNotice; 
  productId :number;
}