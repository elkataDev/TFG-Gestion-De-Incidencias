import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import { useNavigate } from 'react-router-dom';
import './PagAverias.css';

function Selects() {
  return (
    <span className="selects-container">
      <SelectAutoWidth
        inputText="Estado"
        options={[{ label: 'ABIERTA' }, { label: 'EN_PROCESO' }, { label: 'CERRADA' }]}
      />
      <SelectAutoWidth
        inputText="Categoria"
        options={[{ label: 'HARDWARE' }, { label: 'SOFTWARE' }, { label: 'RED' }]}
      />
      <SelectAutoWidth
        inputText="Ubicacion"
        options={[{ label: 'Aula 101' }, { label: 'Aula 203' }]}
      />
    </span>
  );
}

export default function PagAverias() {
  const navigate = useNavigate();
  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averias</h1>
        <BotonPrimario startIcon="" text="+ Nuevo Ticket" onClick={() => navigate('/nuevaAveria')} />
      </div>
      <Selects />
      <div className="table-container">
        <BasicTable
          endpoint="http://localhost:5555/api/incidencias"
          filasPorPagina={5}
          extraColumns={['acciones']}
          renderCustomCell={(key, value, row) => {
            if (key === 'estado') return <EstadoBadge estado={String(value)} />;
            if (key === 'acciones') return <BotonSecundario text="Ver Detalle" onClick={() => navigate(`/averias/${row.id}`)} />;
            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
