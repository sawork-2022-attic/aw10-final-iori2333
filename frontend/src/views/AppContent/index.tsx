import React, { useCallback, useContext, useEffect } from 'react';

import { fetchCart, fetchProducts, newCart } from '../../api';
import { Item, Product } from '../../models';
import CartContainer from '../../components/CartContainer';
import ProductContainer from '../../components/ProductContainer';
import { AppContext } from '../../components/AppStateProvider';

import './index.scss';

function AppContent() {
  const [state, dispatch] = useContext(AppContext);

  const createCart = useCallback(async () => {
    const cart = await newCart();
    return cart.cartId;
  }, []);

  const updateProducts = useCallback(async () => {
    const products = await fetchProducts();
    dispatch({ type: 'SET_PRODUCTS', payload: { products } });
    return products;
  }, [dispatch]);

  const updateCart = useCallback(
    async (products: Product[]) => {
      const cart = await fetchCart(state.cartId);
      const items = cart
        .map(dto => {
          const product = products.find(p => p.id === dto.productId);
          return {
            product,
            quantity: dto.quantity
          };
        })
        .filter(item => item.product != undefined) as Item[];
      dispatch({ type: 'SET_CART', payload: { cart: items } });
    },
    [dispatch]
  );

  // On first load, fetch the products and cart
  useEffect(() => {
    createCart()
      .then(cartId => {
        dispatch({ type: 'NEW_CART', payload: { cartId } });
      })
      .then(() => updateProducts())
      .then(products => updateCart(products))
      .catch(err => console.log(err));
  }, [updateCart, updateProducts]);

  return (
    <main className="app-content">
      <div className="content-products">
        <ProductContainer />
      </div>
      <div className="content-cart">
        <CartContainer items={state.cart} />
      </div>
    </main>
  );
}

export default React.memo(AppContent);
