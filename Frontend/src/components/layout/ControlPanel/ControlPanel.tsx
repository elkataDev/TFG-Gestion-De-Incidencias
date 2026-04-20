import './ControlPanel.css';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import { useNavigate } from 'react-router-dom';

interface ControlPanelProps {
  titulo: string;
  metricsActivos?: React.ReactNode;
  metricsAverias?: React.ReactNode;
}

export default function ControlPanel(props: ControlPanelProps) {
  const navigate = useNavigate();
  const role = localStorage.getItem('role') ?? '';

  return (
    <div className="control-panel-wrapper">
      <h1 className="control-panel-title">{props.titulo}</h1>
      
      {props.metricsActivos && (
        <div className="breakdown-primary-container">
          {props.metricsActivos}
        </div>
      )}

      {props.metricsAverias && (
        <div className="breakdown-secondary-container">
          {props.metricsAverias}
        </div>
      )}

      <div className="control-panel-footer">
        <h2>Acciones Rápidas</h2>
        <div className="button-container">
          {role === 'ADMIN' && (
            <BotonPrimario
              text="Nuevo Activo"
              onClick={() => {
                void navigate('/nuevoActivo');
              }}
            />
          )}
          <BotonSecundario
            onClick={() => {
              void navigate('/nuevaAveria');
            }}
            text="Nueva Averia"
          />
        </div>
      </div>
    </div>
  );
}
