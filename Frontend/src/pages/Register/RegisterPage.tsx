import { RegisterForm } from '@/components/features/usuarios/RegisterForm/RegisterForm';
import { Box, Typography } from '@mui/material';

export const RegisterPage = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        width: '100vw',
        backgroundColor: 'var(--color-bg-body)',
        color: 'var(--color-text-primary)'
      }}
    >
      <Typography variant="h4" fontWeight="bold" mb={4} sx={{ color: 'var(--color-text-primary)' }}>
        Crear Cuenta
      </Typography>
      <RegisterForm />
    </Box>
  );
};
