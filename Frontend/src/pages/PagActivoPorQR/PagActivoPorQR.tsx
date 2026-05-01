import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { apiJson } from '@/services/api/apiService';
import './PagActivoPorQR.css';

interface ActivoQr {
  id: number;
  nombre: string;
  descripcion: string;
  categoria: string;
  estado: string;
  codigoQR: string;
  nombreAula: string;
}

export default function PagActivoPorQR() {
  const { codigoQR } = useParams<{ codigoQR: string }>();
  const navigate = useNavigate();

  const [activo, setActivo] = useState<ActivoQr | null>(null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    void (async () => {
      if (!codigoQR) {
        setErrorMsg('Código QR no válido');
        setLoading(false);
        return;
      }

      try {
        const data = (await apiJson(`/inventario/qr/${encodeURIComponent(codigoQR)}`)) as ActivoQr;
        setActivo(data);
      } catch (error) {
        setErrorMsg(error instanceof Error ? error.message : 'No se pudo cargar el activo');
      } finally {
        setLoading(false);
      }
    })();
  }, [codigoQR]);

  return (
    <div className="pag-activo-qr">
      <div className="activo-qr-card">
        <span className="activo-qr-eyebrow">Código QR detectado</span>
        <h1>Activo escaneado</h1>

        {loading && <p className="activo-qr-muted">Cargando activo...</p>}
        {!loading && errorMsg && <p className="activo-qr-error">{errorMsg}</p>}

        {!loading && activo && (
          <>
            <div className="activo-qr-summary">
              <div>
                <span className="activo-qr-label">Nombre</span>
                <strong>{activo.nombre}</strong>
              </div>
              <EstadoBadge estado={activo.estado} />
            </div>

            <div className="activo-qr-grid">
              <div className="activo-qr-field activo-qr-field-wide">
                <span>Descripción</span>
                <p>{activo.descripcion || 'Sin descripción'}</p>
              </div>
              <div className="activo-qr-field">
                <span>Categoría</span>
                <p>{activo.categoria}</p>
              </div>
              <div className="activo-qr-field">
                <span>Aula</span>
                <p>{activo.nombreAula || 'Sin asignar'}</p>
              </div>
              <div className="activo-qr-field activo-qr-field-wide">
                <span>Código QR</span>
                <p className="activo-qr-code">{activo.codigoQR}</p>
              </div>
            </div>

            <div className="activo-qr-actions">
              <BotonPrimario
                text="Reportar avería de este activo"
                onClick={() => void navigate(`/nuevaAveria?activoId=${String(activo.id)}`)}
              />
              <BotonPrimario text="Volver" onClick={() => void navigate('/')} />
            </div>
          </>
        )}
      </div>
    </div>
  );
}
