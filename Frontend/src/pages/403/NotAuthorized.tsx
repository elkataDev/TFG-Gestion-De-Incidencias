import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import Icon from './exclamacion.png';
import './NotAuthorized.css';
import { HomeIcon } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function Unauthorized() {
  const navigate = useNavigate();

  return (
    <div className="NotAuthorized-Container">
      <img src={Icon} alt="Algo" />
      <h1>403</h1>
      <h2>SIN ACCESO A LA PAGINA</h2>
      <p>
        Lo sentimos, no tienes permisos para acceder a esta página. Por favor contacta con un
        administrador o regresa al panel principal
      </p>
      <BotonPrimario
        startIcon={<HomeIcon />}
        text="Volver al inicio"
        onClick={() => {
          void navigate('/');
        }}
      ></BotonPrimario>
    </div>
  );
}
