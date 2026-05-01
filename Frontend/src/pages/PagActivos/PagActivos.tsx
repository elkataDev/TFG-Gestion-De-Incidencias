import { useEffect, useState } from 'react';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BasicTable from '@/components/common/Tabla/Tabla';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { useNavigate } from 'react-router-dom';
import { apiFetch, apiJson } from '@/services/api/apiService';
import './PagActivos.css';

interface Activo extends Record<string, unknown> {
  id: number;
  nombre: string;
  descripcion: string;
  codigoQR: string;
  categoria: string;
  estado: string;
  fechaIngreso: string;
  aulaId: number;
}

export default function PagActivos() {
  const navigate = useNavigate();

  const [allActivos, setAllActivos] = useState<Activo[]>([]);
  const [estadoFilter, setEstadoFilter] = useState<string>('');
  const [categoriaFilter, setCategoriaFilter] = useState<string>('');

  useEffect(() => {
    void (async () => {
      try {
        const data = (await apiJson('/inventario')) as Activo[];
        setAllActivos(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error('Error al cargar activos:', err);
      }
    })();
  }, []);

  const filteredActivos = allActivos.filter((activo) => {
    const matchEstado = !estadoFilter || activo.estado === estadoFilter;
    const matchCategoria = !categoriaFilter || activo.categoria === categoriaFilter;
    return matchEstado && matchCategoria;
  });

  const handleExportCsv = async () => {
    try {
      const response = await apiFetch('/inventario/export/csv');
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'activos.csv';
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Error al exportar activos:', err);
    }
  };

  const handleDownloadQr = async (activo: Activo) => {
    try {
      const response = await apiFetch(`/inventario/${activo.id}/qr`);
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      const safeName = activo.nombre.replace(/[^a-z0-9-_]+/gi, '-').toLowerCase();
      link.href = url;
      link.download = `qr-${safeName || activo.id}.png`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Error al descargar QR:', err);
    }
  };

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Gestión de Activos</h1>
        <div style={{ display: 'flex', gap: '10px' }}>
          <BotonPrimario
            text="Exportar CSV"
            onClick={() => void handleExportCsv()}
          />
          <BotonPrimario
            text="+ Nuevo Activo"
            onClick={() => {
              void navigate('/nuevoActivo');
            }}
          />
        </div>
      </div>
      <span className="selects-container">
        <SelectAutoWidth
          inputText="Estado"
          options={[{ label: 'DISPONIBLE' }, { label: 'EN_USO' }, { label: 'DAÑADO' }]}
          value={estadoFilter}
          onChange={(val) => setEstadoFilter(val)}
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
          value={categoriaFilter}
          onChange={(val) => setCategoriaFilter(val)}
        />
      </span>
      <div className="table-container">
        <BasicTable
          data={filteredActivos}
          filasPorPagina={5}
          extraColumns={['acciones']}
          renderCustomCell={(key, value, row) => {
            if (key === 'acciones') {
              return (
                <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                  <button
                    className="link-editar"
                    onClick={() => void navigate(`/editarActivo/${String(row.id)}`)}
                  >
                    Editar
                  </button>
                  <button
                    className="link-editar"
                    onClick={() => void handleDownloadQr(row as Activo)}
                  >
                    Descargar QR
                  </button>
                </div>
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
