import { useState } from 'react';
import { Input } from '@/components/common/Input/Input';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Box, Typography, Link } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { apiJson } from '@/services/api/apiService';

interface LoginResponse {
  token?: string; // El JWT
  role?: string; // El rol del usuario
  message?: string; // Mensaje en caso de error
}

export const LoginForm = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', password: '' });
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data: LoginResponse = await apiJson('/auth/login', {
        method: 'POST',
        body: JSON.stringify(form),
      });

      if (!data.token) {
        alert(data.message ?? 'Credenciales incorrectas');
        setLoading(false);
        return;
      }

      localStorage.setItem('token', data.token);
      localStorage.setItem('role', data.role ?? 'USER');

      alert('Login correcto');
      setLoading(false);

      void navigate('/'); // Redirigir al dashboard
    } catch (error) {
      console.error('Error al conectar con la API', error);
      alert(error instanceof Error ? error.message : 'Error de conexión con el servidor');
      setLoading(false);
    }
  };

  return (
    <Box
      component="form"
      onSubmit={(e) => void handleSubmit(e)}
      sx={{
        width: '100%',
        maxWidth: 400,
        mx: 'auto',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        backgroundColor: 'white',
        paddingInline: 4,
        paddingY: 2,
        borderRadius: 2,
        boxShadow: 3,
        gap: 2,
      }}
    >
      <Input
        label="Usuario"
        name="username"
        value={form.username}
        onChange={handleChange}
        required
      />
      <Input
        label="Contraseña"
        name="password"
        type="password"
        value={form.password}
        onChange={handleChange}
        required
      />
      <BotonPrimario text={loading ? 'Cargando...' : 'Login'} type="submit" />

      {/* Enlace a registro */}
      <Typography variant="body2" sx={{ mt: 1 }}>
        ¿No tienes cuenta?{' '}
        <Link component="button" variant="body2" onClick={() => void navigate('/registro')}>
          Regístrate aquí
        </Link>
      </Typography>
    </Box>
  );
};
