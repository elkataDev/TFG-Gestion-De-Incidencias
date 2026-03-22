import { useEffect, useState } from 'react';
import ControlPanel from '@/./components/layout/ControlPanel/ControlPanel';
import CP_Card from '@/./components/layout/ControlPanel/CP_Card';

interface Incidencia {
  id: number;
  estado: string;
}

export default function PagPanelControl() {
  const [incidencias, setIncidencias] = useState<Incidencia[]>([]);
  const role = localStorage.getItem('role') || 'USER';

  useEffect(() => {
    const fetchIncidencias = async () => {
      const token = localStorage.getItem('token');
      try {
        const url = role === 'ADMIN' || role === 'TECNICO' 
          ? 'http://localhost:5555/api/incidencias' 
          : 'http://localhost:5555/api/incidencias/mis-incidencias';
        
        const res = await fetch(url, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        if (res.ok) {
          const data = await res.json();
          setIncidencias(data);
        }
      } catch (e) {
        console.error('Error fetching incidencias', e);
      }
    };
    void fetchIncidencias();
  }, [role]);

  const total = incidencias.length;
  const pendientes = incidencias.filter(i => i.estado === 'ABIERTO').length;
  const enProceso = incidencias.filter(i => i.estado === 'EN_PROGRESO').length;
  const resueltas = incidencias.filter(i => i.estado === 'RESUELTO').length;

  return (
    <div style={{ display: 'flex', height: '100vh' }}>
      <div style={{ flex: 1, padding: '20px' }}>
        <ControlPanel titulo={role === 'USER' ? 'Mi Panel de Averías' : 'Panel de Control General'}>
          {role !== 'USER' && (
            <>
              <CP_Card titulo="Total de activos" numb={1250} />
              <CP_Card titulo="Activos disponibles" numb={980} />
              <CP_Card titulo="Activos en reparacion" numb={270} />
            </>
          )}
          
          <CP_Card titulo="Total de averías" numb={total} />
          
          <CP_Card
            titulo={role === 'USER' ? 'Mis Estadísticas' : 'Estado de averias'}
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
