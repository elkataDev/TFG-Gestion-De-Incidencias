import { Routes, Route } from 'react-router-dom';
import './App.css';
import PagPanelControl from './pages/PagPanelControl/PagPanelControl';
import MainLayout from './components/layout/MainLayout/MainLayout';
import PagInventario from './pages/PagInventario/PagInventario';
import NotFound from './pages/404/NotFound';
import '../src/styles/modern-normalize-main/modern-normalize.css';

function App() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index path="/" element={<PagPanelControl />} />
        <Route path="inventario" element={<PagInventario />} />
      </Route>

      <Route path="*" element={<NotFound></NotFound>} />
    </Routes>
  );
}

export default App;
