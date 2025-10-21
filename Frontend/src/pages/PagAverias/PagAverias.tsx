import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import SelectAutoWidth from '@/components/common/Select/Select';
import './PagAverias.css';

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

export default function PagAverias() {
  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averias</h1>
        <BotonPrimario startIcon="" text="+ Nuevo Ticket"></BotonPrimario>
      </div>
      <Selects />
      <div className="table-container">
        <BasicTable></BasicTable>
      </div>
    </div>
  );
}
