import { Product } from './product';

export interface ItemDto {
  productId: string;
  quantity: number;
}

export interface Item {
  product: Product;
  quantity: number;
}
