import { useState } from 'react';
import { Input } from '@/components/common/Input/Input';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Box, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { apiFetch } from '@/services/api/apiService';

export const RegisterForm = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', email: '', password: '', confirmPassword: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (form.password !== form.confirmPassword) {
      setError('Las contraseñas no coinciden');
      return;
    }

    if (form.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    setLoading(true);

    try {
      const response = await apiFetch('/auth/registro', {
        method: 'POST',
        body: JSON.stringify({
          nombreUsuario: form.username,
          email: form.email,
          password: form.password,
        }),
      });

      await response.text();

      setSuccessMsg('Registro correcto, ahora puedes iniciar sesión');
      setLoading(false);

      setTimeout(() => void navigate('/login'), 1500);
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
        label="Email"
        name="email"
        type="email"
        value={form.email}
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
      <Input
        label="Confirmar Contraseña"
        name="confirmPassword"
        type="password"
        value={form.confirmPassword}
        onChange={handleChange}
        required
      />
      {error && <Alert severity="error" sx={{ width: '100%' }}>{error}</Alert>}
      {successMsg && <Alert severity="success" sx={{ width: '100%' }}>{successMsg}</Alert>}

      <BotonPrimario text={loading ? 'Cargando...' : 'Registrarse'} type="submit" />
    </Box>
  );
};
