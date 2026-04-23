import { useEffect, useState } from 'react';
import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { SelectAulas } from '@/components/common/SelectsBD/SelectAula';
import { useNavigate } from 'react-router-dom';
import API_BASE_URL from '@/services/api/apiConfig';
import { apiJson } from '@/services/api/apiService';
import './PagAverias.css';

interface Incidencia {
  id: number;
  titulo: string;
  descripcion: string;
  estado: string;
  categoria: string;
  nombreAula?: string;
  [key: string]: unknown;
}

export default function PagAverias() {
  const navigate = useNavigate();
  const role = localStorage.getItem('role') ?? 'USUARIO';
  const isAdminOrTech = role === 'ADMIN' || role === 'TECNICO';

  const [estado, setEstado] = useState<string | undefined>();
  const [categoria, setCategoria] = useState<string | undefined>();
  const [nombreAula, setNombreAula] = useState<string | undefined>();
  const [fetchKey, setFetchKey] = useState(0);

  // Para USUARIO: cargamos sus incidencias y filtramos en cliente
  const [misIncidencias, setMisIncidencias] = useState<Incidencia[]>([]);
  const [filteredData, setFilteredData] = useState<Incidencia[]>([]);

  useEffect(() => {
    if (!isAdminOrTech) {
      apiJson('/incidencias/mis-incidencias')
        .then((data) => {
          const list = Array.isArray(data) ? (data as Incidencia[]) : [];
          setMisIncidencias(list);
          setFilteredData(list);
        })
        .catch(console.error);
    }
  }, [isAdminOrTech]);

  // Filtrado en cliente para rol USUARIO
  useEffect(() => {
    if (!isAdminOrTech) {
      let filtered = [...misIncidencias];
      if (estado) filtered = filtered.filter((i) => i.estado === estado);
      if (categoria) filtered = filtered.filter((i) => i.categoria === categoria);
      if (nombreAula) filtered = filtered.filter((i) => i.nombreAula === nombreAula);
      setFilteredData(filtered);
    }
  }, [misIncidencias, estado, categoria, nombreAula, isAdminOrTech]);

  // Endpoint con filtros server-side para ADMIN/TECNICO
  const endpoint = new URL(`${API_BASE_URL}/incidencias/filtrar`);
  if (estado) endpoint.searchParams.append('estado', estado);
  if (categoria) endpoint.searchParams.append('categoria', categoria);
  if (nombreAula) endpoint.searchParams.append('nombreAula', nombreAula);

  useEffect(() => {
    if (isAdminOrTech) {
      setFetchKey((prev) => prev + 1);
    }
  }, [estado, categoria, nombreAula, isAdminOrTech]);

  const renderCells = (key: string, value: unknown, row: Record<string, unknown>) => {
    if (key === 'estado') return <EstadoBadge estado={String(value)} />;
    if (key === 'acciones') {
      return (
        <div style={{ display: 'flex', gap: '8px', justifyContent: 'center' }}>
          <BotonSecundario
            text="Ver Detalle"
            onClick={() => void navigate(`/averias/${String(row.id)}`)}
          />
          {isAdminOrTech && (
            <button
              onClick={async () => {
                if (!window.confirm('¿Eliminar esta avería definitivamente?')) return;
                try {
                  await apiJson(`/incidencias/${String(row.id)}`, { method: 'DELETE' });
                  setFetchKey((prev) => prev + 1);
                } catch {
                  alert('Error al eliminar la avería');
                }
              }}
              style={{
                backgroundColor: 'red',
                color: 'white',
                border: 'none',
                padding: '5px 10px',
                borderRadius: '4px',
                cursor: 'pointer',
                fontFamily: 'inherit',
              }}
            >
              Eliminar
            </button>
          )}
        </div>
      );
    }
    return value as React.ReactNode;
  };

  const tieneFiltroBuscado = Boolean(estado || categoria || nombreAula);
  const sinAverias = !isAdminOrTech && filteredData.length === 0 && !tieneFiltroBuscado;

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averías</h1>
        <BotonPrimario
          startIcon=""
          text="+ Nuevo Ticket"
          onClick={() => void navigate('/nuevaAveria')}
        />
      </div>

      {/* Filtros visibles para todos los roles */}
      <span className="selects-container">
        <SelectAutoWidth
          inputText="Estado"
          options={[{ label: 'ABIERTO' }, { label: 'EN_PROGRESO' }, { label: 'RESUELTO' }]}
          value={estado}
          onChange={setEstado}
        />
        <SelectAutoWidth
          inputText="Categoría"
          options={[{ label: 'HARDWARE' }, { label: 'SOFTWARE' }, { label: 'RED' }]}
          value={categoria}
          onChange={setCategoria}
        />
        <SelectAulas value={nombreAula} onChange={setNombreAula} returnField="name" />
      </span>

      {/* TABLA */}
      <div className="table-container">
        {sinAverias ? (
          <div style={{ textAlign: 'center', padding: '3rem' }}>
            <p style={{ color: 'var(--color-text-secondary)', marginBottom: '1.5rem' }}>
              No tienes averías reportadas aún.
            </p>
            <BotonPrimario
              text="+ Reportar Primera Avería"
              onClick={() => void navigate('/nuevaAveria')}
            />
          </div>
        ) : isAdminOrTech ? (
          <BasicTable
            key={fetchKey}
            endpoint={endpoint.toString()}
            filasPorPagina={5}
            extraColumns={['acciones']}
            renderCustomCell={renderCells}
          />
        ) : (
          <BasicTable
            key={`user-${estado ?? ''}-${categoria ?? ''}-${nombreAula ?? ''}`}
            data={filteredData as Record<string, unknown>[]}
            filasPorPagina={5}
            extraColumns={['acciones']}
            renderCustomCell={renderCells}
          />
        )}
      </div>
    </div>
  );
}
