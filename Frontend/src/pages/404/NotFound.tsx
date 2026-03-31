import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import './NotFound.css';
import Icon from './exclamacion.png';
import { HomeIcon } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="NotFound-Container">
      <img className="img-404" src={Icon} alt="Algo" />
      <h1 className="h1-404">404</h1>
      <h2 className="h2-404">Página no encontrada</h2>
      <p className="p-404">
        Lo sentimos, la página que buscas no existe o ha sido movida. Por favor verifica la URL o
        regresa al panel principal
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
