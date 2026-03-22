import './ControlPanel.css';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import { useNavigate } from 'react-router-dom';

type ControlPanelProps = {
  titulo: string;
  children: React.ReactNode;
};

export default function ControlPanel(props: ControlPanelProps) {
  const navigate = useNavigate();

  return (
    <>
      <h1 className="control-panel-title">{props.titulo}</h1>
      <div className="breakdown-gen-container">{props.children}</div>
      <div className="control-panel-footer">
        <h2>Acciones Rapidas</h2>
        <span className="button-container">
          <BotonPrimario text="Nuevo Activo"></BotonPrimario>
          <BotonSecundario
            onClick={() => {
              void navigate('nuevaAveria');
            }}
            text="Nueva Averia"
          ></BotonSecundario>
        </span>
      </div>
    </>
  );
}
