import { useEffect, useState } from 'react';
import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { SelectAulas } from '@/components/common/SelectsBD/SelectAula';
import { useNavigate } from 'react-router-dom';
import API_BASE_URL from '@/services/api/apiConfig';
import './PagAverias.css';

export default function PagAverias() {
  const navigate = useNavigate();

  const [estado, setEstado] = useState<string | undefined>();
  const [categoria, setCategoria] = useState<string | undefined>();
  const [nombreAula, setNombreAula] = useState<string | undefined>();
  const [fetchKey, setFetchKey] = useState(0);

  const handleChangeEstado = (val?: string) => {
    setEstado(val);
  };
  const handleChangeCategoria = (val?: string) => {
    setCategoria(val);
  };
  const handleChangeAula = (val?: string) => {
    setNombreAula(val);
  };

  // --- Construye endpoint dinámico según filtros ---
  const endpoint = new URL(`${API_BASE_URL}/incidencias/filtrar`);
  if (estado) endpoint.searchParams.append('estado', estado);
  if (categoria) endpoint.searchParams.append('categoria', categoria);
  if (nombreAula) endpoint.searchParams.append('nombreAula', nombreAula);

  // --- Refresca la tabla cada vez que cambia un filtro ---
  useEffect(() => {
    setFetchKey((prev) => prev + 1);
  }, [estado, categoria, nombreAula]);

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averías</h1>
        <BotonPrimario startIcon="" text="+ Nuevo Ticket" onClick={() => navigate('/nuevaAveria')} />
      </div>

      {/* SELECTS */}
      <span className="selects-container">
        <SelectAutoWidth
          inputText="Estado"
          options={[{ label: 'ABIERTO' }, { label: 'EN_PROGRESO' }, { label: 'RESUELTO' }]}
          value={estado}
          onChange={handleChangeEstado}
        />

        <SelectAutoWidth
          inputText="Categoria"
          options={[{ label: 'HARDWARE' }, { label: 'SOFTWARE' }, { label: 'RED' }]}
          value={categoria}
          onChange={handleChangeCategoria}
        />

        <SelectAulas value={nombreAula} onChange={handleChangeAula} />
      </span>

      {/* TABLA */}
      <div className="table-container">
        <BasicTable
          key={fetchKey}
          endpoint={endpoint.toString()}
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
