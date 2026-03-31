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
  const role = localStorage.getItem('role') ?? 'USUARIO';

  useEffect(() => {
    const fetchData = async () => {
      try {
        const endpointInc =
          role === 'ADMIN' || role === 'TECNICO' ? '/incidencias' : '/incidencias/mis-incidencias';

        const dataInc = await apiJson(endpointInc);
        setIncidencias(dataInc as Incidencia[]);

        if (role === 'ADMIN' || role === 'TECNICO') {
          const dataAct = await apiJson('/inventario');
          setActivos(dataAct as Activo[]);
        }
      } catch (e) {
        console.error('Error fetching data', e);
      }
    };
    void fetchData();
  }, [role]);

  const total = incidencias.length;
  const pendientes = incidencias.filter((i) => i.estado === 'ABIERTO').length;
  const enProceso = incidencias.filter((i) => i.estado === 'EN_PROGRESO').length;
  const resueltas = incidencias.filter((i) => i.estado === 'RESUELTO').length;

  const totalActivos = activos.length;
  const activosDisponibles = activos.filter((a) => a.estado === 'DISPONIBLE').length;
  const activosDanados = activos.filter((a) => a.estado === 'DANADO').length;

  return (
    <div style={{ display: 'flex', height: '100vh' }}>
      <div style={{ flex: 1, padding: '20px' }}>
        <ControlPanel
          titulo={role === 'USUARIO' ? 'Mi Panel de Averías' : 'Panel de Control General'}
        >
          {role !== 'USUARIO' && (
            <>
              <CP_Card titulo="Total de activos" numb={totalActivos} />
              <CP_Card titulo="Activos disponibles" numb={activosDisponibles} />
              <CP_Card titulo="Activos dañados" numb={activosDanados} />
            </>
          )}

          <CP_Card titulo="Total de averías" numb={total} />

          <CP_Card
            titulo={role === 'USUARIO' ? 'Mis Estadísticas' : 'Estado de averías'}
            numb={total}
            progress={[
              { label: 'Pendientes', percent: total ? Math.round((pendientes / total) * 100) : 0 },
              { label: 'En proceso', percent: total ? Math.round((enProceso / total) * 100) : 0 },
              { label: 'Resueltas', percent: total ? Math.round((resueltas / total) * 100) : 0 },
            ]}
          />
        </ControlPanel>
      </div>
    </div>
  );
}
