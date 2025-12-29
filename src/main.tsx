import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './styles/modern-normalize.css';
import './App.css';
import App from './App';
import { BrowserRouter } from 'react-router-dom';

const rootElement = document.getElementById('root');

if (!rootElement) {
  throw new Error('Root element not found');
}

// Cambia según quieras probar o ejecutar la app normal
const USE_PLAYGROUND = import.meta.env.VITE_USE_PLAYGROUND === 'true';

createRoot(rootElement).render(
  <BrowserRouter>
    <StrictMode>
      <App />
    </StrictMode>
  </BrowserRouter>
);
