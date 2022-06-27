import axios from 'axios';
import { ItemDto, Product } from '../models';

export function fetchProducts() {
  return axios.get('/api/products').then(res => res.data as Product[]);
}

export function fetchProduct(id: string) {
  return axios.get(`/api/products/${id}`).then(res => res.data as Product);
}

export function fetchCart() {
  return axios.get('/api/cart').then(res => res.data as ItemDto[]);
}

export function addToCart(productId: string) {
  return axios
    .post(`/api/cart/${productId}`)
    .then(res => res.data as ItemDto[]);
}

export function removeFromCart(productId: string) {
  return axios
    .delete(`/api/cart/${productId}`)
    .then(res => res.data as ItemDto[]);
}

export function modifyCart(productId: string, quantity: number) {
  return axios
    .put(
      `/api/cart/${productId}`,
      {},
      {
        params: { quantity: quantity }
      }
    )
    .then(res => res.data as ItemDto[]);
}

export function fetchCartItem(productId: string) {
  return axios.get(`/api/cart/${productId}`).then(res => res.data as ItemDto);
}
