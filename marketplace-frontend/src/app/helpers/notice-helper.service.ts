import { Injectable } from '@angular/core';
import { ProductNotices } from '../interfaces/product-notice';

@Injectable({
  providedIn: 'root'
})
export class NoticeHelperService {

  constructor() {}

  
  isExpired(notice: ProductNotices): boolean {
    if (!notice.expireDate) return false;

    const today = new Date();
    const expiration = new Date(notice.expireDate);

    today.setHours(0, 0, 0, 0);
    expiration.setHours(0, 0, 0, 0);

    return today.getTime() > expiration.getTime();
  }


  isValidExpirationDate(dateStr: string): boolean {
    if (!dateStr) return true; 

    const parts = dateStr.split('-'); // YYYY-MM-DD
    const selected = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
    const today = new Date();

    selected.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    return selected >= today;
  }
}