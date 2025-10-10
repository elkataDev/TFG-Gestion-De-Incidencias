import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { AppPrueba } from './AppPrueba';
import { BrowserRouter } from 'react-router-dom';

const rootElement = document.getElementById('root');

if (!rootElement) {
  throw new Error('Root element not found');
}

createRoot(rootElement).render(
  <BrowserRouter>
    <StrictMode>
      <AppPrueba />
    </StrictMode>
  </BrowserRouter>
);
