import React, { useContext, useMemo } from 'react';
import { AppContext } from '../AppStateProvider';
import Container from '../Container';
import ProductTile from '../ProductTile';

function ProductContainer() {
  const [state] = useContext(AppContext);
  const products = useMemo(
    () => state.products.filter(it => it.name.includes(state.filter)),
    [state.products, state.filter]
  );
  return (
    <Container title="Products">
      {products.map((it, index) => (
        <ProductTile key={`${it.id}#${index}'}`} product={it} />
      ))}
      {!products.length && <div>No products found</div>}
    </Container>
  );
}

export default React.memo(ProductContainer);
