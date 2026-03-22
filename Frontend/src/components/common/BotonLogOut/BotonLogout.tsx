import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';

const LogoutButton = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('tokenExpiry');

    void navigate('/Login', { replace: true });
  };

  return (
    <Button variant="contained" color="error" onClick={handleLogout}>
      Cerrar sesión
    </Button>
  );
};

export default LogoutButton;
