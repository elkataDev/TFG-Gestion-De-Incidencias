import Button from '@mui/material/Button';
import type { ButtonPrimarioProps } from '../BotonPrimario/BotonPrimario';
import './BotonSecundario.css';

export default function BotonSecundario({
  text,
  disabled = false,
  fullWidth = false,
  onClick,
  startIcon,
  endIcon,
  type = 'button',
  size = 'medium',
}: ButtonPrimarioProps) {
  return (
    <Button
      variant="outlined"
      color="secondary"
      disabled={disabled}
      fullWidth={fullWidth}
      onClick={onClick}
      startIcon={startIcon}
      endIcon={endIcon}
      type={type}
      size={size}
      className="app-btn app-btn--secondary"
    >
      {text}
    </Button>
  );
}
