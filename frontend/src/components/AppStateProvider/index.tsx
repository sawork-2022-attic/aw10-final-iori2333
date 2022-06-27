import React, { Dispatch, useCallback, useMemo, useReducer } from 'react';
import { Item, Product } from '../../models';

export interface AppState {
  filter: string;
  products: Product[];
  cart: Item[];
}

type ActionMap = {
  ADD_CART: { productId: string };
  REMOVE_CART: { productId: string };
  MODIFY_CART: { productId: string; quantity: number };
  SET_FILTER: { filter: string };
  SET_PRODUCTS: { products: Product[] };
  SET_CART: { cart: Item[] };
};

export type Action<T = keyof ActionMap> = T extends keyof ActionMap
  ? { type: T; payload: ActionMap[T] }
  : never;

export const AppContext = React.createContext<[AppState, Dispatch<Action>]>([
  {
    filter: '',
    products: [],
    cart: []
  },
  () => {}
]);

function AppStateProvider(props: { children: React.ReactNode }) {
  const initState = useMemo(() => {
    return {
      filter: '',
      products: [],
      cart: []
    };
  }, []);

  const reducer = useCallback((state: AppState, action: Action) => {
    switch (action.type) {
      case 'SET_CART': {
        const { cart } = action.payload;
        return {
          ...state,
          cart
        };
      }
      case 'SET_PRODUCTS': {
        const { products } = action.payload;
        return {
          ...state,
          products
        };
      }
      case 'ADD_CART': {
        const { productId } = action.payload;
        const product = state.products.find(p => p.id === productId);
        if (product) {
          const item = state.cart.find(i => i.product.id === productId);
          if (item) {
            return {
              ...state,
              cart: state.cart.map(i =>
                i.product.id === productId
                  ? {
                      product: product,
                      quantity: i.quantity + 1
                    }
                  : i
              )
            };
          } else {
            return {
              ...state,
              cart: [...state.cart, { product, quantity: 1 }]
            };
          }
        }
        return state;
      }
      case 'MODIFY_CART': {
        const { productId, quantity } = action.payload;
        if (quantity <= 0) {
          return {
            ...state,
            cart: state.cart.filter(i => i.product.id !== productId)
          };
        }
        return {
          ...state,
          cart: state.cart.map(i =>
            i.product.id === productId
              ? {
                  product: i.product,
                  quantity
                }
              : i
          )
        };
      }
      case 'REMOVE_CART': {
        const { productId } = action.payload;
        return {
          ...state,
          cart: state.cart.filter(i => i.product.id !== productId)
        };
      }
      case 'SET_FILTER': {
        const { filter } = action.payload;
        return {
          ...state,
          filter
        };
      }
      default:
        return state;
    }
  }, []);

  const [state, dispatch] = useReducer(reducer, initState);

  return (
    <AppContext.Provider value={[state, dispatch]}>
      {props.children}
    </AppContext.Provider>
  );
}

export default React.memo(AppStateProvider);
