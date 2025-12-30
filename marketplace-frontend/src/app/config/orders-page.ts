/**
 * Configurazione centralizzata della pagina Ordini.
 * 
 * Questo file definisce, in base al ruolo dell'utente (SELLER o CUSTOMER),
 * il titolo da visualizzare nella pagina e l'endpoint REST da utilizzare
 * per il recupero degli ordini associati al profilo.
 * L'obiettivo è separare la configurazione dalla logica dei componenti,
 * evitando condizioni ripetute e rendendo il codice più manutenibile
 * ed estendibile a nuovi ruoli.
 */

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
