import Button from '@mui/material/Button';

export interface ButtonPrimarioProps {
  text: string;
  disabled?: boolean;
  fullWidth?: boolean;

  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;

  /** Icono al inicio (usando un componente de MUI o personalizado) */
  startIcon?: React.ReactNode;

  /** Icono al final */
  endIcon?: React.ReactNode;

  /** Tipo de botón (para formularios) */
  type?: 'button' | 'submit' | 'reset';

  /** Tamaño visual */
  size?: 'small' | 'medium' | 'large';
}

export default function BotonPrimario({
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
      variant="contained"
      color="primary"
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
        fontWeight: 600,
        paddingX: 2.5,
        paddingY: 1,
      }}
    >
      {text}
    </Button>
  );
}
