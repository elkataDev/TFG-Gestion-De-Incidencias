import { useState } from 'react';
import { Input } from '@/components/common/Input/Input';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

interface DummyLoginResponse {
  id: number;
  username: string;
  email: string;
  token: string;
}

export const LoginForm = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', password: '' });

  // Mapa de roles para simularlo en dummy
  const roleMap: Record<string, string> = {
    kminchelle: 'ROLE_USER',
    emilys: 'ROLE_TECNICO',
    admin: 'ROLE_ADMIN',
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const payload = {
      username: form.username.trim(),
      password: form.password.trim(),
    };
    console.log('Enviando payload:', payload);

    try {
      const response = await fetch('https://dummyjson.com/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
        mode: 'cors',
      });

      if (!response.ok) {
        alert('Credenciales incorrectas');
        return;
      }

      const data: DummyLoginResponse = await response.json();
      console.log('Login correcto', data.username);

      // Guardar token
      localStorage.setItem('token', data.token);

      // Simular rol según username
      const role = roleMap[data.username] ?? 'ROLE_USER';
      localStorage.setItem('role', role);

      console.log('Rol asignado:', role);

      // Redirigir al dashboard
      void navigate('/');
    } catch (error) {
      console.error('Error de conexión', error);
      alert('Error de conexión');
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
      <BotonPrimario text="Login" type="submit" />
    </Box>
  );
};
