import { Input } from '@/components/common/Input/Input';
import './PagNuevaAveria.css';
import TextArea from '@/components/common/TextArea/TextArea';
import SelectAutoWidth from '@/components/common/Select/Select';
import { useNavigate } from 'react-router-dom';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';

export default function PagNuevaAveria() {
  const navigate = useNavigate();
  return (
    <div className="pag-nueva-averia">
      <div className="report-container">
        <h1>Reportar Nueva Averia</h1>
        <p>Complete el siguiente formulario para reportar un problema técnico</p>
        <TextArea placeHolder="Describa la averia en detalle..." minRows={10}></TextArea>

        <span className="select-container">
          <h3>Tipo de Archivo</h3>
          <SelectAutoWidth
            inputText="Seleccionar Tipo de Activo"
            options={[{ label: 'Activo1' }, { label: 'Activo2' }]}
          ></SelectAutoWidth>
        </span>

        <span className="select-container">
          <h3>Ubicacion</h3>
          <SelectAutoWidth
            inputText="Seleccionar Tipo de Activo"
            options={[{ label: 'Activo1' }, { label: 'Activo2' }]}
          ></SelectAutoWidth>
        </span>

        <span>
          <BotonPrimario
            onClick={() => {
              void navigate('/');
            }}
            text="Volver al inicio"
          ></BotonPrimario>

          <BotonPrimario
            onClick={() => {
              '';
            }}
            text="Enviar"
          ></BotonPrimario>
        </span>
      </div>
    </div>
  );
}
