import './PagUsarios.css';
import BasicTable from '@/components/common/Tabla/Tabla';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';

export default function PagUsuarios() {
  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Gestión de Usuarios</h1>
      </div>
      <div className="table-container">
        <BasicTable
          endpoint="/usuarios"
          filasPorPagina={5}
          renderCustomCell={(key, value, _row) => {
            if (key === 'rol') return <EstadoBadge estado={String(value)} />;
            if (key === 'activo') return <EstadoBadge estado={value ? 'ACTIVO' : 'INACTIVO'} />;
            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
