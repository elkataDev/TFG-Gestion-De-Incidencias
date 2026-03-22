import BotonPrimario from '../BotonPrimario/BotonPrimario';
import './Paginacion.css';

type PaginacionProps = {
  total: number;
  filasPorPagina: number;
  paginaActual: number;
  onPageChange?: (page: number) => void;
};

const Paginacion: React.FC<PaginacionProps> = ({
  total,
  filasPorPagina,
  paginaActual,
  onPageChange,
}) => {
  const paginasTotales = Math.ceil(total / filasPorPagina);

  return (
    <div className="botones-paginacion">
      <BotonPrimario text="Primera Pagina" onClick={() => onPageChange?.(0)} size="small" />

      <BotonPrimario
        text="Anterior"
        disabled={paginaActual === 0}
        onClick={() => onPageChange?.(paginaActual - 1)}
        size="small"
      />

      <BotonPrimario
        text="Siguiente"
        disabled={paginaActual === paginasTotales - 1}
        onClick={() => onPageChange?.(paginaActual + 1)}
        size="small"
      />

      <BotonPrimario
        text="Ultima Pagina"
        onClick={() => onPageChange?.(paginasTotales - 1)}
        size="small"
      />
    </div>
  );
};

export default Paginacion;
