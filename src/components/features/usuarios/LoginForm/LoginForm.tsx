import { useState } from 'react';
import { Input } from '@/components/common/Input/Input';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Box } from '@mui/material';

interface LoginResponse {
  ok: boolean;
  token?: string;
  message?: string;
}

export const LoginForm = () => {
  const [form, setForm] = useState({ email: '', password: '' });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:3000/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(form),
      });

      const data: LoginResponse = await response.json();

      if (!response.ok || !data.ok) {
        alert(data.message ?? 'Credenciales incorrectas');
        return;
      }

      console.log('Login correcto');
      console.log('Token:', data.token);

      localStorage.setItem('token', data.token!);
    } catch (error) {
      console.error('Error al conectar con la API', error);
      alert('Error de conexión');
    }
  };

  return (
    <Box
      component="form"
      onSubmit={(e) => {
        void handleSubmit(e);
      }}
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
        label="Correo electrónico"
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
      <BotonPrimario text="Login" type="submit" />
    </Box>
  );
};
