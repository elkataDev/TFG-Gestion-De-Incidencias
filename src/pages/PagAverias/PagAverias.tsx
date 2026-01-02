import { useEffect, useState } from 'react';
import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { SelectAulas } from '@/components/common/SelectsBD/SelectAula';
import './PagAverias.css';

export default function PagAverias() {
  const handleChangeEstado = (val?: string) => {
    setEstado(val);
    setFetchKey((f) => f + 1);
  };
  const handleChangeCategoria = (val?: string) => {
    setCategoria(val);
    setFetchKey((f) => f + 1);
  };
  const handleChangeAula = (val?: string) => {
    setNombreAula(val);
    setFetchKey((f) => f + 1);
  };

  const [estado, setEstado] = useState<string | undefined>();
  const [categoria, setCategoria] = useState<string | undefined>();
  const [nombreAula, setNombreAula] = useState<string | undefined>();
  const [fetchKey, setFetchKey] = useState(0); // Para forzar refetch
  // --- Construye endpoint dinámico según filtros ---
  const endpoint = new URL('http://localhost:5555/api/incidencias/filtrar');

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
        <BotonPrimario text="+ Nuevo Ticket" />
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
          key={fetchKey} // Cada vez que cambie fetchKey, refetch
          endpoint={endpoint.toString()}
          filasPorPagina={5}
          renderCustomCell={(key, value) =>
            key === 'estado' ? <EstadoBadge estado={String(value)} /> : (value as React.ReactNode)
          }
        />
      </div>
    </div>
  );
}
