import React, { useCallback } from 'react';
import Container from '../Container';
import { Item } from '../../models';
import CartItem from '../CartItem';
import TextButton from '../TextButton';

import './index.scss';
import { checkout } from '../../api';

function CartContainer({ items }: { items: Item[] }) {
  const onCheckout = useCallback(() => {
    checkout(
      items.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    ).then(r => console.log(r));
  }, [items]);
  return (
    <>
      <Container title="Cost" className="checkout-container">
        <div className="cost">
          <span className="cost-text">Total Cost</span>
          <span>
            ￥
            {items.reduce(
              (acc, item) => acc + item.product.price * item.quantity,
              0
            )}
          </span>
        </div>
        <TextButton
          onClick={onCheckout}
          className="checkout-btn"
          text="Checkout"
        />
      </Container>
      <Container className="cart-container" title="Cart">
        {items.map((item: Item) => (
          <CartItem key={item.product.id} item={item} />
        ))}
        {!items.length && <div>Your cart is empty</div>}
      </Container>
    </>
  );
}

export default React.memo(CartContainer);
