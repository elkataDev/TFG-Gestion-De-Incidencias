import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import './PagUsarios.css';
import BasicTable from '@/components/common/Tabla/Tabla';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';

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

export default function PagUsuarios() {
  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averias</h1>
        <BotonPrimario startIcon="" text="+ Nuevo Ticket"></BotonPrimario>
      </div>
      <Selects />
      <div className="table-container">
        <BasicTable
          endpoint="/usuarios"
          filasPorPagina={5}
          renderCustomCell={(key, value, _row) => {
            if (key === 'estado' || key === 'rol') return <EstadoBadge estado={String(value)} />;
            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
