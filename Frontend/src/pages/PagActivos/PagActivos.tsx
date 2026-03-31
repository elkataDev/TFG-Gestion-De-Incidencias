import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BasicTable from '@/components/common/Tabla/Tabla';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { useNavigate } from 'react-router-dom';

export default function PagActivos() {
  const navigate = useNavigate();

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Gestión de Activos</h1>
        <BotonPrimario
          text="+ Nuevo Activo"
          onClick={() => {
            void navigate('/nuevoActivo');
          }}
        />
      </div>
      <span className="selects-container">
        <SelectAutoWidth
          inputText="Estado"
          options={[{ label: 'DISPONIBLE' }, { label: 'EN_USO' }, { label: 'DANADO' }]}
        />
        <SelectAutoWidth
          inputText="Categoría"
          options={[
            { label: 'COMPUTADORA' },
            { label: 'IMPRESORA' },
            { label: 'PROYECTOR' },
            { label: 'MONITOR' },
            { label: 'RED' },
            { label: 'SERVIDOR' },
            { label: 'PERIFERICO' },
            { label: 'SEGURIDAD' },
          ]}
        />
      </span>
      <div className="table-container">
        <BasicTable
          endpoint="/inventario"
          filasPorPagina={5}
          extraColumns={['acciones']}
          renderCustomCell={(key, value, row) => {
            if (key === 'acciones') {
              return (
                <button
                  className="link-editar"
                  onClick={() => void navigate(`/editarActivo/${String(row.id)}`)}
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
