import React from 'react';
import AppHeader from './views/AppHeader';
import AppContent from './views/AppContent';
import AppStateProvider from './components/AppStateProvider';

function App() {
  return (
    <AppStateProvider>
      <AppHeader />
      <AppContent />
    </AppStateProvider>
  );
}

export default App;
