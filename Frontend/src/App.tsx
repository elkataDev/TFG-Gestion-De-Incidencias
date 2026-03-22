import { Routes, Route } from 'react-router-dom';
import './App.css';
import PrivateRoute from './components/common/PrivateRoute/PrivateRoute';
import Unauthorized from './pages/403/NotAuthorized';
import PagPanelControl from './pages/PagPanelControl/PagPanelControl';
import MainLayout from './components/layout/MainLayout/MainLayout';
import PagInventario from './pages/PagInventario/PagInventario';
import NotFound from './pages/404/NotFound';
import './styles/modern-normalize.css';
import PagAverias from './pages/PagAverias/PagAverias';
import PagNuevaAveria from './pages/PagNuevaAveria/PagNuevaAveria';
import PagUsuarios from './pages/PagUsuarios/PagUsuarios';
import PagActivos from './pages/PagActivos/PagActivos';
import PagNuevoActivo from './pages/PagNuevoActivo/PagNuevoActivo';
import PagDetalleAveria from './pages/PagDetalleAveria/PagDetalleAveria';
import EditarActivo from './pages/PagEditarActivo/PagEditarActivo';
import { LoginPage } from './pages/Login/LoginPage';
import { RegisterForm } from './components/features/usuarios/RegisterForm/RegisterForm';

function App() {
  return (
    <Routes>
      {/* ================= RUTAS PÚBLICAS ================= */}
      <Route path="/registro" element={<RegisterForm></RegisterForm>}></Route>
      <Route path="/login" element={<LoginPage />} />
      <Route path="*" element={<NotFound />} />
      <Route path="/unauthorized" element={<Unauthorized />} />

      {/* ================= RUTAS CON LAYOUT ================= */}
      <Route
        path="/"
        element={
          <PrivateRoute allowedRoles={['USER', 'TECNICO', 'ADMIN']}>
            <MainLayout />
          </PrivateRoute>
        }
      >
        {/* Panel */}
        <Route
          index
          element={
            <PrivateRoute allowedRoles={['USER', 'TECNICO', 'ADMIN']}>
              <PagPanelControl />
            </PrivateRoute>
          }
        />

        {/* Averías */}
        <Route
          path="averias"
          element={
            <PrivateRoute allowedRoles={['USER', 'TECNICO', 'ADMIN']}>
              <PagAverias />
            </PrivateRoute>
          }
        />
        
        {/* Detalle Avería */}
        <Route
          path="averias/:id"
          element={
            <PrivateRoute allowedRoles={['USER', 'TECNICO', 'ADMIN']}>
              <PagDetalleAveria />
            </PrivateRoute>
          }
        />

        {/* Inventario */}
        <Route
          path="inventario"
          element={
            <PrivateRoute allowedRoles={['TECNICO', 'ADMIN']}>
              <PagInventario />
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
      </Route>

      {/* ================= RUTAS SIN LAYOUT ================= */}

      {/* Crear avería */}
      <Route
        path="/nuevaAveria"
        element={
          <PrivateRoute allowedRoles={['USER', 'TECNICO', 'ADMIN']}>
            <PagNuevaAveria />
          </PrivateRoute>
        }
      />

      {/* Crear activo */}
      <Route
        path="/nuevoActivo"
        element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <PagNuevoActivo />
          </PrivateRoute>
        }
      />

      {/* Editar activo */}
      <Route
        path="/editarActivo/:id"
        element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <EditarActivo />
          </PrivateRoute>
        }
      />
    </Routes>
  );
}

export default App;
