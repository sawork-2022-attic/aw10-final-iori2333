import axios from 'axios';
import { ItemDto, Product } from '../models';

export function newCart() {
  return axios.get('/api/cart/new').then(res => res.data as { cartId: string });
}

export function fetchProducts() {
  return axios.get('/api/products').then(res => res.data as Product[]);
}

export function fetchProduct(id: string) {
  return axios.get(`/api/products/${id}`).then(res => res.data as Product);
}

export function fetchCart(cartId: string) {
  return axios.get(`/api/cart/${cartId}`).then(res => res.data as ItemDto[]);
}

export function addToCart(cartId: string, productId: string) {
  return axios
    .post(`/api/cart/${cartId}/${productId}`)
    .then(res => res.data as ItemDto[]);
}

export function removeFromCart(cartId: string, productId: string) {
  return axios
    .delete(`/api/cart/${cartId}/${productId}`)
    .then(res => res.data as ItemDto[]);
}

export function modifyCart(
  cartId: string,
  productId: string,
  quantity: number
) {
  return axios
    .put(
      `/api/cart/${cartId}/${productId}`,
      {},
      {
        params: { quantity: quantity }
      }
    )
    .then(res => res.data as ItemDto[]);
}

export function fetchCartItem(cartId: string, productId: string) {
  return axios
    .get(`/api/cart/${cartId}/${productId}`)
    .then(res => res.data as ItemDto);
}

export function checkout(cart: ItemDto[]) {
  return axios.post('/api/order', cart, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
}
