
// Rappresenta un ordine nel marketplace.
export interface Order {
  id: number;
  date: string;
  total: number;
  status: string;
}