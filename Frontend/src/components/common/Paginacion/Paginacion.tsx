import BotonPrimario from '../BotonPrimario/BotonPrimario';
import './Paginacion.css';

interface PaginacionProps {
  total: number;
  filasPorPagina: number;
  paginaActual: number;
  onPageChange?: (page: number) => void;
}

const Paginacion: React.FC<PaginacionProps> = ({
  total,
  filasPorPagina,
  paginaActual,
  onPageChange,
}) => {
  const paginasTotales = Math.ceil(total / filasPorPagina);

  return (
    <div className="botones-paginacion">
      <BotonPrimario text="Primera Página" onClick={() => onPageChange?.(0)} size="small" />

      <BotonPrimario
        text="Anterior"
        disabled={paginaActual === 0}
        onClick={() => onPageChange?.(paginaActual - 1)}
        size="small"
      />

      <span className="page-indicator" style={{ 
        display: 'flex', 
        alignItems: 'center', 
        padding: '0 12px',
        fontWeight: 500,
        color: 'var(--color-text-secondary, #666)'
      }}>
        Página {paginaActual + 1} de {paginasTotales}
      </span>

      <BotonPrimario
        text="Siguiente"
        disabled={paginaActual === paginasTotales - 1}
        onClick={() => onPageChange?.(paginaActual + 1)}
        size="small"
      />

      <BotonPrimario
        text="Última Página"
        onClick={() => onPageChange?.(paginasTotales - 1)}
        size="small"
      />
    </div>
  );
};

export default Paginacion;
