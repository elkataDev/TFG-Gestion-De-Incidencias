import Button from '@mui/material/Button';
import type { ButtonPrimarioProps } from '../BotonPrimario/BotonPrimario';

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
      sx={{
        borderRadius: 2,
        textTransform: 'none',
        fontWeight: 500,
        paddingX: 2.5,
        paddingY: 1,
        borderWidth: 2,
        '&:hover': {
          borderWidth: 2,
          backgroundColor: 'rgba(0, 0, 0, 0.04)',
        },
      }}
    >
      {text}
    </Button>
  );
}
