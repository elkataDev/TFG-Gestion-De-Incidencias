import { useState } from 'react';
import { Input } from '@/components/common/Input/Input';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { apiFetch } from '@/services/api/apiService';

export const RegisterForm = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', email: '', password: '', confirmPassword: '' });
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (form.password !== form.confirmPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    setLoading(true);

    try {
      const response = await apiFetch('/auth/registro', {
        method: 'POST',
        body: JSON.stringify({
          nombreUsuario: form.username, // 🔴 IMPORTANTE
          email: form.email,
          password: form.password,
        }),
      });

      const text = await response.text();
      console.log('Respuesta cruda:', text, 'Status:', response.status);

      alert('Registro correcto, ahora puedes iniciar sesión');
      setLoading(false);

      void navigate('/login');
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
      <BotonPrimario text={loading ? 'Cargando...' : 'Registrarse'} type="submit" />
    </Box>
  );
};
