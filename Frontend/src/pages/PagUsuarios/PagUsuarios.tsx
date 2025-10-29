import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import './PagUsarios.css';
import TablaUsuario, {
  UsuarioData,
} from '@/components/features/usuarios/TablaUsuario/TablaUsuario';

const data: UsuarioData[] = [
  {
    id: '1',
    nombre: 'Juan Perez',
    correo: 'juan.perez@example.com',
    rol: 'admin',
    estado: 'activo',
  },
  {
    id: '2',
    nombre: 'Maria Gomez',
    correo: 'maria.gomez@example.com',
    rol: 'user',
    estado: 'inactivo',
  },
  {
    id: '3',
    nombre: 'Juan Perez',
    correo: 'juan.perez@example.com',
    rol: 'admin',
    estado: 'activo',
  },
  {
    id: '4',
    nombre: 'Maria Gomez',
    correo: 'maria.gomez@example.com',
    rol: 'user',
    estado: 'inactivo',
  },
  {
    id: '5',
    nombre: 'Juan Perez',
    correo: 'juan.perez@example.com',
    rol: 'admin',
    estado: 'activo',
  },
  {
    id: '6',
    nombre: 'Maria Gomez',
    correo: 'maria.gomez@example.com',
    rol: 'user',
    estado: 'inactivo',
  },
];

export default function PagUsuarios() {
  return (
    <div className="pag-usuarios-container">
      <div className="header-container">
        <h2>Listado de Usuarios</h2>
        <BotonPrimario startIcon="" text="+ Nuevo Usuario" />
      </div>

      <div className="table-container">
        <TablaUsuario usuarios={data} />
      </div>
    </div>
  );
}
