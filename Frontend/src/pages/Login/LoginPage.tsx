//import { LoginForm } from '@/components/features/usuarios/LoginForm/LoginForm';
import { LoginForm } from '@/components/features/usuarios/LoginForm/LoginForm';
import { Box, Typography } from '@mui/material';

export const LoginPage = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        width: '100vw',
        backgroundColor: 'transparent',
        color: 'var(--color-text-primary)'
      }}
    >
      <Typography
        variant="h4"
        fontWeight="bold"
        mb={3}
        sx={{ color: 'var(--color-text-primary)', letterSpacing: '0.01em' }}
      >
        Bienvenido
      </Typography>
      <LoginForm />
    </Box>
  );
};
