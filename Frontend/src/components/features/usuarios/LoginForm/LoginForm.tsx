import { useState } from 'react';
import { Input } from '@/components/common/Input/Input';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Box, Typography, Link, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { apiJson } from '@/services/api/apiService';

interface LoginResponse {
  token?: string;
  username?: string;
  role?: string;
  message?: string;
}

export const LoginForm = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const data: LoginResponse = await apiJson('/auth/login', {
        method: 'POST',
        body: JSON.stringify(form),
      });

      if (!data.token) {
        setError(data.message ?? 'Credenciales incorrectas');
        setLoading(false);
        return;
      }

      localStorage.setItem('token', data.token);
      localStorage.setItem('username', data.username ?? form.username);
      localStorage.setItem('role', data.role ?? 'USUARIO');

      setLoading(false);
      window.location.href = '/'; // Forzar recarga total para limpiar estados antiguos
    } catch (err) {
      console.error('Error al conectar con la API', err);
      setError(err instanceof Error ? err.message : 'Error de conexión con el servidor');
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
      {error && <Alert severity="error" sx={{ width: '100%' }}>{error}</Alert>}

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
