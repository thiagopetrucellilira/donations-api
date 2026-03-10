import { User } from './user.model';

export enum DonationStatus {
  AVAILABLE = 'AVAILABLE',
  RESERVED = 'RESERVED',
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED'
}

export enum FoodCategory {
  GRAOS_CEREAIS = 'GRAOS_CEREAIS',
  HORTIFRUTI = 'HORTIFRUTI',
  LATICINIOS = 'LATICINIOS',
  PROTEINAS = 'PROTEINAS',
  ENLATADOS_CONSERVAS = 'ENLATADOS_CONSERVAS',
  BEBIDAS = 'BEBIDAS',
  PADARIA_CONFEITARIA = 'PADARIA_CONFEITARIA',
  TEMPEROS_CONDIMENTOS = 'TEMPEROS_CONDIMENTOS',
  REFEICAO_PRONTA = 'REFEICAO_PRONTA',
  OUTROS = 'OUTROS'
}

export const FOOD_CATEGORY_LABELS: Record<FoodCategory, string> = {
  [FoodCategory.GRAOS_CEREAIS]: 'Grãos e Cereais',
  [FoodCategory.HORTIFRUTI]: 'Hortifruti',
  [FoodCategory.LATICINIOS]: 'Laticínios',
  [FoodCategory.PROTEINAS]: 'Proteínas',
  [FoodCategory.ENLATADOS_CONSERVAS]: 'Enlatados e Conservas',
  [FoodCategory.BEBIDAS]: 'Bebidas',
  [FoodCategory.PADARIA_CONFEITARIA]: 'Padaria e Confeitaria',
  [FoodCategory.TEMPEROS_CONDIMENTOS]: 'Temperos e Condimentos',
  [FoodCategory.REFEICAO_PRONTA]: 'Refeição Pronta',
  [FoodCategory.OUTROS]: 'Outros'
};

export interface Donation {
  id?: number;
  title: string;
  description: string;
  category: FoodCategory;
  quantity: number;
  expirationDate?: string;
  perishable?: boolean;
  storageInstructions?: string;
  location?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  status?: DonationStatus;
  imageUrls?: string;
  pickupInstructions?: string;
  expiresAt?: Date;
  createdAt?: Date;
  updatedAt?: Date;
  donor?: User;
}

export interface DonationRequest {
  title: string;
  description: string;
  category: FoodCategory;
  quantity: number;
  expirationDate?: string;
  perishable?: boolean;
  storageInstructions?: string;
  location?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  imageUrls?: string;
  pickupInstructions?: string;
  expiresAt?: Date;
}

export interface DonationFilters {
  category?: FoodCategory;
  city?: string;
  state?: string;
  status?: DonationStatus;
  search?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
