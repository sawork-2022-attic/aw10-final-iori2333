import React, { useCallback, useContext } from 'react';
import { AppContext } from '../AppStateProvider';
import { Product } from '../../models';
import './index.scss';
import { addToCart } from '../../api';
import TextButton from '../TextButton';

function ProductTile({ product }: { product: Product }) {
  const [, dispatch] = useContext(AppContext);
  const onAdd = useCallback(() => {
    addToCart(product.id).then(() =>
      dispatch({
        type: 'ADD_CART',
        payload: { productId: product.id }
      })
    );
  }, [dispatch, product]);

  return (
    <div className="product-tile">
      <img src={product.image} alt={product.name} />
      <div className="info">
        <div className="name">{product.name}</div>
        <div className="price">{product.price}</div>
      </div>
      <TextButton className="add-btn" text="Add" onClick={onAdd} />
    </div>
  );
}

export default React.memo(ProductTile);
