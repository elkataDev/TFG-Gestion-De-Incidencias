import { Routes, Route } from 'react-router-dom';
import './App.css';
import PrivateRoute from './components/common/PrivateRoute/PrivateRoute';
import Unauthorized from './pages/403/NotAuthorized';
import PagPanelControl from './pages/PagPanelControl/PagPanelControl';
import MainLayout from './components/layout/MainLayout/MainLayout';
import NotFound from './pages/404/NotFound';
import './styles/modern-normalize.css';
import PagAverias from './pages/PagAverias/PagAverias';
import PagNuevaAveria from './pages/PagNuevaAveria/PagNuevaAveria';
import PagUsuarios from './pages/PagUsuarios/PagUsuarios';
import PagActivos from './pages/PagActivos/PagActivos';
import PagNuevoActivo from './pages/PagNuevoActivo/PagNuevoActivo';
import PagDetalleAveria from './pages/PagDetalleAveria/PagDetalleAveria';
import EditarActivo from './pages/PagEditarActivo/PagEditarActivo';
import PagActivoPorQR from './pages/PagActivoPorQR/PagActivoPorQR';
import { LoginPage } from './pages/Login/LoginPage';
import { RegisterPage } from './pages/Register/RegisterPage';

function App() {
  return (
    <Routes>
      {/* ================= RUTAS PÚBLICAS ================= */}
      <Route path="/registro" element={<RegisterPage />}></Route>
      <Route path="/login" element={<LoginPage />} />
      <Route path="*" element={<NotFound />} />
      <Route path="/unauthorized" element={<Unauthorized />} />

      {/* ================= RUTAS CON LAYOUT ================= */}
      <Route
        path="/"
        element={
          <PrivateRoute allowedRoles={['USUARIO', 'TECNICO', 'ADMIN']}>
            <MainLayout />
          </PrivateRoute>
        }
      >
        {/* Panel */}
        <Route
          index
          element={
            <PrivateRoute allowedRoles={['USUARIO', 'TECNICO', 'ADMIN']}>
              <PagPanelControl />
            </PrivateRoute>
          }
        />

        {/* Averías */}
        <Route
          path="averias"
          element={
            <PrivateRoute allowedRoles={['USUARIO', 'TECNICO', 'ADMIN']}>
              <PagAverias />
            </PrivateRoute>
          }
        />

        {/* Detalle Avería */}
        <Route
          path="averias/:id"
          element={
            <PrivateRoute allowedRoles={['USUARIO', 'TECNICO', 'ADMIN']}>
              <PagDetalleAveria />
            </PrivateRoute>
          }
        />

        {/* Activos */}
        <Route
          path="activos"
          element={
            <PrivateRoute allowedRoles={['TECNICO', 'ADMIN']}>
              <PagActivos />
            </PrivateRoute>
          }
        />

        {/* Usuarios */}
        <Route
          path="usuarios"
          element={
            <PrivateRoute allowedRoles={['ADMIN']}>
              <PagUsuarios />
            </PrivateRoute>
          }
        />

        {/* Crear avería */}
        <Route
          path="nuevaAveria"
          element={
            <PrivateRoute allowedRoles={['USUARIO', 'TECNICO', 'ADMIN']}>
              <PagNuevaAveria />
            </PrivateRoute>
          }
        />

        {/* Crear activo */}
        <Route
          path="nuevoActivo"
          element={
            <PrivateRoute allowedRoles={['ADMIN']}>
              <PagNuevoActivo />
            </PrivateRoute>
          }
        />

        {/* Editar activo */}
        <Route
          path="editarActivo/:id"
          element={
            <PrivateRoute allowedRoles={['ADMIN']}>
              <EditarActivo />
            </PrivateRoute>
          }
        />

        <Route
          path="activos/qr/:codigoQR"
          element={
            <PrivateRoute allowedRoles={['USUARIO', 'TECNICO', 'ADMIN']}>
              <PagActivoPorQR />
            </PrivateRoute>
          }
        />
      </Route>
    </Routes>
  );
}

export default App;
