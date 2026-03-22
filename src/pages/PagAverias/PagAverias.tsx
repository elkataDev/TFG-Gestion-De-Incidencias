import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
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
  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averias</h1>
        <BotonPrimario startIcon="" text="+ Nuevo Ticket"></BotonPrimario>
      </div>
      <Selects />
      <div className="table-container">
        <BasicTable
          endpoint="http://localhost:5555/api/incidencias"
          filasPorPagina={5}
          renderCustomCell={(key, value, _row) => {
            if (key === 'estado') return <EstadoBadge estado={String(value)} />;
            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
