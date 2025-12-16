export enum UserRole {
  CUSTOMER = 'CUSTOMER',
  SELLER = 'SELLER'
}

export interface RegisterPayload {
  username: string;
  password: string;
  email: string;
  address: string;
  role: UserRole;
}
