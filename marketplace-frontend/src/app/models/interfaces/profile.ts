// Enum dei ruoli possibili di un utente.
export enum UserRole {
  CUSTOMER = 'CUSTOMER',
  SELLER = 'SELLER'
}

// Payload per la registrazione di un nuovo utente
export interface RegisterPayload {
  username: string;
  password: string;
  email: string;
  address: string;
  role: UserRole;
}
