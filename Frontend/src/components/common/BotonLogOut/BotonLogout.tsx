import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';

const LogoutButton = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');

    void navigate('/login', { replace: true });
  };

  return (
    <Button
      variant="outlined"
      onClick={handleLogout}
      sx={{
        m: 2,
        mt: 1,
        borderColor: 'rgba(255, 107, 107, 0.55)',
        color: '#ffdede',
        textTransform: 'none',
        borderRadius: '10px',
        fontWeight: 600,
        '&:hover': {
          borderColor: 'rgba(255, 107, 107, 0.85)',
          backgroundColor: 'rgba(255, 107, 107, 0.12)'
        }
      }}
    >
      Cerrar sesión
    </Button>
  );
};

export default LogoutButton;
