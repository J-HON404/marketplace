export interface OrdersPageConfig {
  title: string;
  endpoint: (profileId: number) => string;
}

export const ORDERS_PAGE_CONFIG: Record<string, OrdersPageConfig> = {
  SELLER: {
    title: 'Ordini ricevuti ',
    endpoint: (profileId: number) => `/api/profiles/${profileId}/orders` 
  },
  CUSTOMER: {
    title: 'Ordini fatti ',
    endpoint: (profileId: number) => `/api/profiles/${profileId}/orders`
  }
};
