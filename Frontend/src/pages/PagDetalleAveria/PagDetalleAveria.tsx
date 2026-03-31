import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import TextArea from '@/components/common/TextArea/TextArea';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import SelectAutoWidth from '@/components/common/Select/Select';
import { apiJson } from '@/services/api/apiService';
import API_BASE_URL from '@/services/api/apiConfig';
import './PagDetalleAveria.css';

interface Incidencia {
  id: number;
  titulo: string;
  descripcion: string;
  estado: string;
  categoria: string;
  nombreAula: string;
  nombreUsuario: string;
  fechaReporte: string;
  fechaCierre: string | null;
  adjuntoUrl: string | null;
}

interface Comentario {
  id: number;
  texto: string;
  fecha: string;
  nombreUsuario: string;
}

interface Historial {
  id: number;
  estadoAnterior: string;
  estadoNuevo: string;
  fechaCambio: string;
  nombreUsuario: string;
}

export default function PagDetalleAveria() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [incidencia, setIncidencia] = useState<Incidencia | null>(null);
  const [comentarios, setComentarios] = useState<Comentario[]>([]);
  const [historial, setHistorial] = useState<Historial[]>([]);
  const [nuevoComentario, setNuevoComentario] = useState('');
  const [nuevoEstado, setNuevoEstado] = useState('');
  const [loading, setLoading] = useState(true);

  const fetchIncidencia = async () => {
    try {
      const [resInc, resCom, resHist] = await Promise.all([
        apiJson(`/incidencias/${id}`),
        apiJson(`/incidencias/${id}/comentarios`),
        apiJson(`/incidencias/${id}/historial`),
      ]);
      setIncidencia(resInc as Incidencia);
      setComentarios(resCom as Comentario[]);
      setHistorial(resHist as Historial[]);
    } catch (err) {
      console.error('Error fetching details', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (id) {
      void fetchIncidencia();
    }
  }, [id]);

  const handleAddComment = async () => {
    if (!nuevoComentario.trim()) return;
    try {
      await apiJson(`/incidencias/${id}/comentarios`, {
        method: 'POST',
        body: JSON.stringify({ texto: nuevoComentario }),
      });
      setNuevoComentario('');
      void fetchIncidencia(); // refresh
    } catch (err) {
      console.error('Error posting comment', err);
    }
  };

  const handleUpdateEstado = async () => {
    if (!nuevoEstado) return;
    try {
      await apiJson(`/incidencias/${id}/estado`, {
        method: 'PATCH',
        body: JSON.stringify({ estado: nuevoEstado }),
      });
      void fetchIncidencia(); // refresh
    } catch (err) {
      console.error('Error updating state', err);
    }
  };

  if (loading) return <div>Cargando detalle...</div>;
  if (!incidencia) return <div>Incidencia no encontrada</div>;

  return (
    <div className="pag-detalle">
      <div className="detalle-header">
        <BotonSecundario text="Volver" onClick={() => void navigate('/averias')} />
        <h1>Detalle de Avería #{incidencia.id}</h1>
      </div>

      <div className="detalle-content">
        <div className="incidencia-info">
          <h2>Información</h2>
          <p>
            <strong>Título:</strong> {incidencia.titulo}
          </p>
          <p>
            <strong>Descripción:</strong> {incidencia.descripcion}
          </p>
          <p>
            <strong>Categoría:</strong> {incidencia.categoria}
          </p>
          <p>
            <strong>Estado:</strong> <EstadoBadge estado={incidencia.estado} />
          </p>
          <p>
            <strong>Reportado por:</strong> {incidencia.nombreUsuario}
          </p>
          <p>
            <strong>Aula:</strong> {incidencia.nombreAula}
          </p>
          <p>
            <strong>Fecha Reporte:</strong> {new Date(incidencia.fechaReporte).toLocaleString()}
          </p>
          {incidencia.adjuntoUrl && (
            <p>
              <strong>Archivo Adjunto:</strong>{' '}
              <a
                href={`${API_BASE_URL}/incidencias/archivos/${incidencia.adjuntoUrl}`}
                target="_blank"
                rel="noopener noreferrer"
              >
                Descargar Archivo
              </a>
            </p>
          )}

          <div className="estado-updater">
            <h3>Actualizar Estado</h3>
            <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
              <SelectAutoWidth
                inputText="Nuevo Estado"
                options={[{ label: 'ABIERTO' }, { label: 'EN_PROGRESO' }, { label: 'RESUELTO' }]}
                value={nuevoEstado}
                onChange={setNuevoEstado}
              />
              <BotonPrimario text="Actualizar" onClick={() => void handleUpdateEstado()} />
            </div>
          </div>
        </div>

        <div className="historial-info">
          <h2>Historial de Estados</h2>
          {historial.length === 0 ? (
            <p>No hay cambios de estado registrados.</p>
          ) : (
            <ul>
              {historial.map((h) => (
                <li key={h.id}>
                  <strong>{new Date(h.fechaCambio).toLocaleString()}:</strong> {h.nombreUsuario}{' '}
                  cambió estado de <EstadoBadge estado={h.estadoAnterior} /> a{' '}
                  <EstadoBadge estado={h.estadoNuevo} />
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="comentarios-section">
          <h2>Comentarios</h2>
          <div className="comentarios-list">
            {comentarios.map((c) => (
              <div key={c.id} className="comentario-item">
                <p className="comentario-meta">
                  <strong>{c.nombreUsuario || 'Usuario'}</strong> -{' '}
                  {c.fecha ? new Date(c.fecha).toLocaleString() : 'Sin fecha'}
                </p>
                <p>{c.texto}</p>
              </div>
            ))}
          </div>
          <div className="nuevo-comentario">
            <TextArea
              placeHolder="Escribe un comentario..."
              value={nuevoComentario}
              onChange={(e) => setNuevoComentario(e.target.value)}
              minRows={3}
            />
            <BotonPrimario text="Añadir Comentario" onClick={() => void handleAddComment()} />
          </div>
        </div>
      </div>
    </div>
  );
}
