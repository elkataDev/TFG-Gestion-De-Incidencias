import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { HomeIcon } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import Icon from './exclamacion.png';
import './NotAuthorized.css';
export default function Unauthorized() {
  const navigate = useNavigate();
  return (
    <div>
      <img src={Icon} alt="" />
      <h1>403 - Acceso denegado</h1>
      <p>No tienes permisos para acceder a esta página.</p>
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
