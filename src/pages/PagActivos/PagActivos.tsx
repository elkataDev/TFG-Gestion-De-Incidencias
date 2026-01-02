import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BasicTable from '@/components/common/Tabla/Tabla';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { useNavigate } from 'react-router-dom';

function Selects() {
  return (
    <span className="selects-container">
      <SelectAutoWidth
        inputText="Estado"
        options={[{ label: 'Opcion1' }, { label: 'Opcion2' }]}
      ></SelectAutoWidth>
      <SelectAutoWidth
        inputText="Categoria"
        options={[{ label: 'Opcion1' }, { label: 'Opcion2' }]}
      ></SelectAutoWidth>
      <SelectAutoWidth
        inputText="Ubicacion"
        options={[{ label: 'Opcion1' }, { label: 'Opcion2' }]}
      ></SelectAutoWidth>
    </span>
  );
}

export default function PagActivos() {
  const navigate = useNavigate();

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averias</h1>
        <BotonPrimario
          startIcon=""
          text="+ Nuevo Activo"
          onClick={() => {
            void navigate('/nuevoActivo');
          }}
        ></BotonPrimario>
      </div>
      <Selects />
      <div className="table-container">
        <BasicTable
          endpoint="http://localhost:5555/api/inventario"
          filasPorPagina={5}
          extraColumns={['acciones']}
          renderCustomCell={(key, value, row) => {
            if (key === 'acciones') {
              return (
                <button
                  className="link-editar"
                  onClick={() => void navigate(`/editarActivo/${row.id}`)}
                >
                  Editar
                </button>
              );
            }

            if (key === 'estado') {
              return <EstadoBadge estado={String(value)} />;
            }

            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
