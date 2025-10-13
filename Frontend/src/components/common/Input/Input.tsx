import { useState } from 'react';
import { TextField, InputAdornment, IconButton } from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import './Input.css';

export interface InputProps {
  label: string;
  name: string;

  type?: 'text' | 'email' | 'password' | 'number' | 'search' | 'tel' | 'url';

  /** Valor actual del input (para formularios controlados) */
  value?: string;

  /** Evento que se ejecuta al cambiar el valor */
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;

  required?: boolean;

  fullWidth?: boolean;

  /** Texto de ayuda o validación bajo el campo */
  helperText?: string;

  /** Marca el input como inválido (para mostrar error visual) */
  error?: boolean;

  disabled?: boolean;

  /** Muestra un botón de mostrar/ocultar contraseña si es de tipo password */
  showPasswordToggle?: boolean;
}

export const Input = ({
  label,
  name,
  type = 'text',
  value,
  onChange,
  required = false,
  fullWidth = true,
  helperText,
  error = false,
  disabled = false,
  showPasswordToggle = true,
}: InputProps) => {
  const [showPassword, setShowPassword] = useState(false);
  const isPassword = type === 'password';

  return (
    <TextField
      id={name}
      name={name}
      label={label}
      type={isPassword && !showPassword ? 'password' : 'text'}
      value={value}
      onChange={onChange}
      required={required}
      fullWidth={fullWidth}
      helperText={helperText}
      error={error}
      disabled={disabled}
      variant="outlined"
      margin="normal"
      slotProps={
        isPassword && showPasswordToggle
          ? {
              input: {
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={() => setShowPassword(!showPassword)}
                      edge="end"
                      tabIndex={-1}
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              },
            }
          : undefined
      }
    />
  );
};
