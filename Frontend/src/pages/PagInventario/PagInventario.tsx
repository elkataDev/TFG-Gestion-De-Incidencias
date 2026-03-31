import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { useNavigate } from 'react-router-dom';
import '@/pages/PagActivos/PagActivos.css';

export default function PagInventario() {
  const navigate = useNavigate();

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Gestión de Inventario</h1>
        <BotonPrimario
          startIcon=""
          text="+ Nuevo Activo"
          onClick={() => void navigate('/nuevoActivo')}
        />
      </div>

      <div className="table-container">
        <BasicTable
          endpoint="/inventario"
          filasPorPagina={5}
          extraColumns={['acciones']}
          renderCustomCell={(key, value, row) => {
            if (key === 'estado') return <EstadoBadge estado={String(value)} />;
            if (key === 'acciones')
              return (
                <BotonSecundario
                  text="Editar"
                  onClick={() => void navigate(`/editarActivo/${String(row.id)}`)}
                />
              );
            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
