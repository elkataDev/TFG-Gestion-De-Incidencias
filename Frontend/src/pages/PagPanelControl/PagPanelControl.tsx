import { useEffect, useState } from 'react';
import ControlPanel from '@/./components/layout/ControlPanel/ControlPanel';
import CP_Card from '@/./components/layout/ControlPanel/CP_Card';
import { apiJson } from '@/services/api/apiService';

interface Incidencia {
  id: number;
  estado: string;
}

interface Activo {
  id: number;
  estado: string;
}

export default function PagPanelControl() {
  const [incidencias, setIncidencias] = useState<Incidencia[]>([]);
  const [activos, setActivos] = useState<Activo[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Siempre cargamos TODAS las incidencias del sistema para todos los roles
        const dataInc = await apiJson('/incidencias');
        setIncidencias(dataInc as Incidencia[]);

        // Inventario también para todos
        const dataAct = await apiJson('/inventario');
        setActivos(dataAct as Activo[]);
      } catch (e) {
        console.error('Error fetching data', e);
      }
    };
    void fetchData();
  }, []);

  const total = incidencias.length;
  const pendientes = incidencias.filter((i) => i.estado === 'ABIERTO').length;
  const enProceso = incidencias.filter((i) => i.estado === 'EN_PROGRESO').length;
  const resueltas = incidencias.filter((i) => i.estado === 'RESUELTO').length;

  const totalActivos = activos.length;
  const activosDisponibles = activos.filter((a) => a.estado === 'DISPONIBLE').length;
  const activosDanados = activos.filter((a) => a.estado === 'DAÑADO').length;

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-background-secondary)' }}>
      <div style={{ flex: 1, overflowY: 'auto' }}>
        <ControlPanel
          titulo="Panel de Control General"
          metricsActivos={
            <>
              <CP_Card titulo="Total de activos" numb={totalActivos} />
              <CP_Card titulo="Activos disponibles" numb={activosDisponibles} />
              <CP_Card titulo="Activos dañados" numb={activosDanados} />
            </>
          }
          metricsAverias={
            <>
              <CP_Card titulo="Total de averías" numb={total} />
              <CP_Card
                titulo="Estado de averías"
                numb={total}
                progress={[
                  { label: 'Pendientes', percent: total ? Math.round((pendientes / total) * 100) : 0 },
                  { label: 'En proceso', percent: total ? Math.round((enProceso / total) * 100) : 0 },
                  { label: 'Resueltas', percent: total ? Math.round((resueltas / total) * 100) : 0 },
                ]}
              />
            </>
          }
        />
      </div>
    </div>
  );
}
