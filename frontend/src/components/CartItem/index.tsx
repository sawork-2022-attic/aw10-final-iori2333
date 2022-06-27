import React, { useCallback, useContext } from 'react';
import { Item } from '../../models';
import './index.scss';
import { AppContext } from '../AppStateProvider';
import { modifyCart, removeFromCart } from '../../api';

function CartItem({ item }: { item: Item }) {
  const [, dispatch] = useContext(AppContext);

  const onModify = useCallback(
    (quantity: number) => {
      modifyCart(item.product.id, item.quantity + quantity).then(
        () =>
          dispatch({
            type: 'MODIFY_CART',
            payload: {
              productId: item.product.id,
              quantity: item.quantity + quantity
            }
          }),
        err => console.log(err)
      );
    },
    [dispatch, item]
  );

  const onRemove = useCallback(() => {
    removeFromCart(item.product.id).then(
      () =>
        dispatch({
          type: 'REMOVE_CART',
          payload: {
            productId: item.product.id
          }
        }),
      err => console.log(err)
    );
  }, [dispatch, item]);

  return (
    <div className="cart-item">
      <span className="item-name">{item.product.name}</span>
      <span className="button-set">
        <button className="norm-btn" onClick={() => onModify(-1)}>
          -
        </button>
        <span>{item.quantity}</span>
        <button className="norm-btn" onClick={() => onModify(1)}>
          +
        </button>
        <button className="remove-btn" onClick={onRemove}>
          <div className="codicon codicon-trash" />
        </button>
      </span>
    </div>
  );
}

export default React.memo(CartItem);
