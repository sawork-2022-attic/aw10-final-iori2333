import React, { useCallback, useContext } from 'react';
import { AppContext } from '../../components/AppStateProvider';

import './index.scss';

function AppHeader() {
  const [, dispatch] = useContext(AppContext);
  const handleInput = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) =>
      dispatch({
        type: 'SET_FILTER',
        payload: { filter: e.target.value }
      }),
    [dispatch]
  );

  return (
    <header className="app-header">
      <span>MicroPos</span>
      <div className="search-bar">
        <div className="codicon codicon-search" />
        <input placeholder="Filter product..." onInput={handleInput} />
      </div>
    </header>
  );
}

export default React.memo(AppHeader);
