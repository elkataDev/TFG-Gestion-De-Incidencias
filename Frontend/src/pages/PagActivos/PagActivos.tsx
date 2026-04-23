import { useEffect, useState } from 'react';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BasicTable from '@/components/common/Tabla/Tabla';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { useNavigate } from 'react-router-dom';
import { apiJson } from '@/services/api/apiService';

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
