export enum ShopCategory {
  CASA_ARREDAMENTO = 'CASA_ARREDAMENTO',
  MODA_ACCESSORI = 'MODA_ACCESSORI',
  TECNOLOGIA_ELETTRONICA = 'TECNOLOGIA_ELETTRONICA',
  SALUTE_FITNESS = 'SALUTE_FITNESS'
}

export interface Shop {
  id: number;
  name: string;
  shopCategory: ShopCategory;
  profileId: number;
}