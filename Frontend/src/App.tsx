import { Routes, Route } from 'react-router-dom';
import './App.css';
import PagPanelControl from './pages/PagPanelControl/PagPanelControl';
import MainLayout from './components/layout/MainLayout/MainLayout';
import PagInventario from './pages/PagInventario/PagInventario';
import NotFound from './pages/404/NotFound';
import './styles/modern-normalize.css';
import PagAverias from './pages/PagAverias/PagAverias';
import PagNuevaAveria from './pages/PagNuevaAveria/PagNuevaAveria';

function App() {
  return (
    <Routes>
      {
        //Rutas Con Sidebar
      }
      <Route path="/" element={<MainLayout />}>
        <Route index path="/" element={<PagPanelControl />} />
        <Route path="inventario" element={<PagInventario />} />
        <Route path="averias" element={<PagAverias></PagAverias>} />

        <Route path="usuarios" element={<PagInventario></PagInventario>} />
      </Route>

      {
        //Rutas Sin SideBar
      }
      <Route path="*" element={<NotFound></NotFound>} />
      <Route path="/nuevaAveria" element={<PagNuevaAveria></PagNuevaAveria>} />
    </Routes>
  );
}

export default App;
