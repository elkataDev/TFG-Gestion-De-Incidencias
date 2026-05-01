import { useEffect, useState, useCallback } from 'react';
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
  esInterno: boolean;
}

interface Historial {
  id: number;
  estadoAnterior: string;
  estadoNuevo: string;
  fechaCambio: string;
  nombreUsuario: string;
}

interface ParteTrabajo {
  id: number;
  nombreTecnico: string;
  tiempoHoras: number;
  descripcionTrabajo: string;
  piezasUtilizadas: string;
  fechaRegistro: string;
}

const ESTADOS_DISPONIBLES = [
  { label: 'ABIERTO' },
  { label: 'EN_CURSO' },
  { label: 'EN_ESPERA' },
  { label: 'RESUELTO' },
  { label: 'CERRADO' },
  { label: 'REABIERTO' },
];

export default function PagDetalleAveria() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [incidencia, setIncidencia] = useState<Incidencia | null>(null);
  const [comentarios, setComentarios] = useState<Comentario[]>([]);
  const [historial, setHistorial] = useState<Historial[]>([]);
  const [partes, setPartes] = useState<ParteTrabajo[]>([]);
  const [nuevoComentario, setNuevoComentario] = useState('');
  const [comentarioEsInterno, setComentarioEsInterno] = useState(false);
  const [nuevoEstado, setNuevoEstado] = useState('');
  const [loading, setLoading] = useState(true);

  // Parte de trabajo
  const [tiempoHoras, setTiempoHoras] = useState('');
  const [descripcionTrabajo, setDescripcionTrabajo] = useState('');
  const [piezasUtilizadas, setPiezasUtilizadas] = useState('');
  const [guardandoParte, setGuardandoParte] = useState(false);
  const [parteError, setParteError] = useState('');

  const role = localStorage.getItem('role') ?? 'USUARIO';
  const isAdminOrTech = role === 'ADMIN' || role === 'TECNICO';

  const fetchAll = useCallback(async () => {
    if (!id) return;
    try {
      const requests: Promise<unknown>[] = [
        apiJson(`/incidencias/${id}`),
        apiJson(`/incidencias/${id}/comentarios`),
        apiJson(`/incidencias/${id}/historial`),
      ];
      if (isAdminOrTech) {
        requests.push(apiJson(`/incidencias/${id}/partes`));
      }
      const results = await Promise.all(requests);
      setIncidencia(results[0] as Incidencia);
      setComentarios(results[1] as Comentario[]);
      setHistorial(results[2] as Historial[]);
      if (isAdminOrTech && results[3]) {
        setPartes(results[3] as ParteTrabajo[]);
      }
    } catch (err) {
      console.error('Error fetching details', err);
    } finally {
      setLoading(false);
    }
  }, [id, isAdminOrTech]);

  useEffect(() => {
    void fetchAll();
  }, [fetchAll]);

  const handleAddComment = async () => {
    if (!nuevoComentario.trim()) return;
    try {
      await apiJson(`/incidencias/${id}/comentarios`, {
        method: 'POST',
        body: JSON.stringify({
          texto: nuevoComentario,
          esInterno: isAdminOrTech ? String(comentarioEsInterno) : 'false',
        }),
      });
      setNuevoComentario('');
      setComentarioEsInterno(false);
      void fetchAll();
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
      void fetchAll();
    } catch (err) {
      console.error('Error updating state', err);
    }
  };

  const handleAddParte = async () => {
    setParteError('');
    const horas = parseFloat(tiempoHoras);
    if (!tiempoHoras || isNaN(horas) || horas < 0.1) {
      setParteError('El tiempo mínimo es 0.1 horas.');
      return;
    }
    try {
      setGuardandoParte(true);
      await apiJson(`/incidencias/${id}/partes`, {
        method: 'POST',
        body: JSON.stringify({
          tiempoHoras: horas,
          descripcionTrabajo,
          piezasUtilizadas,
        }),
      });
      setTiempoHoras('');
      setDescripcionTrabajo('');
      setPiezasUtilizadas('');
      void fetchAll();
    } catch (err) {
      setParteError('Error al guardar el parte de trabajo.');
      console.error(err);
    } finally {
      setGuardandoParte(false);
    }
  };

  if (loading) return <div className="pag-detalle-loading">Cargando detalle...</div>;
  if (!incidencia) return <div className="pag-detalle-loading">Incidencia no encontrada</div>;

  const totalHoras = partes.reduce((sum, p) => sum + (p.tiempoHoras ?? 0), 0);

  return (
    <div className="pag-detalle">
      <div className="detalle-header">
        <BotonSecundario text="← Volver" onClick={() => void navigate('/averias')} />
        <h1>Ticket #{incidencia.id} — {incidencia.titulo}</h1>
        <EstadoBadge estado={incidencia.estado} />
      </div>

      <div className="detalle-grid">
        {/* ─── Columna Izquierda ─── */}
        <div className="detalle-main">
          {/* Info básica */}
          <section className="detalle-card">
            <h2>Información del Ticket</h2>
            <div className="info-grid">
              <div className="info-item"><span className="info-label">Descripción</span><p>{incidencia.descripcion}</p></div>
              <div className="info-item"><span className="info-label">Categoría</span><p>{incidencia.categoria}</p></div>
              <div className="info-item"><span className="info-label">Reportado por</span><p>{incidencia.nombreUsuario}</p></div>
              <div className="info-item"><span className="info-label">Aula</span><p>{incidencia.nombreAula}</p></div>
              <div className="info-item">
                <span className="info-label">Fecha Reporte</span>
                <p>{new Date(incidencia.fechaReporte).toLocaleString()}</p>
              </div>
              {incidencia.fechaCierre && (
                <div className="info-item">
                  <span className="info-label">Fecha Cierre</span>
                  <p>{new Date(incidencia.fechaCierre).toLocaleString()}</p>
                </div>
              )}
              {incidencia.adjuntoUrl && (
                <div className="info-item">
                  <span className="info-label">Archivo Adjunto</span>
                  <a
                    href={`${API_BASE_URL}/incidencias/archivos/${incidencia.adjuntoUrl}`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="adjunto-link"
                  >
                    📎 Descargar Archivo
                  </a>
                </div>
              )}
            </div>
          </section>

          {/* Cambio de estado — solo técnicos/admin */}
          {isAdminOrTech && (
            <section className="detalle-card">
              <h2>Cambiar Estado</h2>
              <div className="estado-updater">
                <SelectAutoWidth
                  inputText="Nuevo Estado"
                  options={ESTADOS_DISPONIBLES}
                  value={nuevoEstado}
                  onChange={setNuevoEstado}
                />
                <BotonPrimario
                  text="Actualizar Estado"
                  onClick={() => void handleUpdateEstado()}
                />
              </div>
            </section>
          )}

          {/* Partes de trabajo — solo técnicos/admin */}
          {isAdminOrTech && (
            <section className="detalle-card">
              <h2>
                Partes de Trabajo
                <span className="parte-total-badge">Total: {totalHoras.toFixed(1)}h</span>
              </h2>

              {partes.length === 0 ? (
                <p className="sin-datos">No hay partes de trabajo registrados aún.</p>
              ) : (
                <div className="partes-lista">
                  {partes.map((p) => (
                    <div key={p.id} className="parte-item">
                      <div className="parte-header">
                        <span className="parte-tecnico">🔧 {p.nombreTecnico}</span>
                        <span className="parte-horas">{p.tiempoHoras}h</span>
                        <span className="parte-fecha">{new Date(p.fechaRegistro).toLocaleDateString()}</span>
                      </div>
                      {p.descripcionTrabajo && <p className="parte-desc">{p.descripcionTrabajo}</p>}
                      {p.piezasUtilizadas && (
                        <p className="parte-piezas">📦 Piezas: {p.piezasUtilizadas}</p>
                      )}
                    </div>
                  ))}
                </div>
              )}

              <div className="nuevo-parte">
                <h3>Registrar Nuevo Parte</h3>
                <div className="parte-form">
                  <div className="parte-field">
                    <label htmlFor="tiempoHoras">Tiempo invertido (horas) *</label>
                    <input
                      id="tiempoHoras"
                      type="number"
                      min="0.1"
                      step="0.25"
                      placeholder="ej: 1.5"
                      value={tiempoHoras}
                      onChange={(e) => setTiempoHoras(e.target.value)}
                      className="parte-input"
                    />
                  </div>
                  <div className="parte-field">
                    <label htmlFor="descripcionTrabajo">Descripción del trabajo</label>
                    <TextArea
                      placeHolder="Describe las tareas realizadas..."
                      value={descripcionTrabajo}
                      onChange={(e) => setDescripcionTrabajo(e.target.value)}
                      minRows={2}
                    />
                  </div>
                  <div className="parte-field">
                    <label htmlFor="piezasUtilizadas">Piezas/materiales utilizados</label>
                    <input
                      id="piezasUtilizadas"
                      type="text"
                      placeholder="ej: 2x RAM 8GB, 1x Cable HDMI"
                      value={piezasUtilizadas}
                      onChange={(e) => setPiezasUtilizadas(e.target.value)}
                      className="parte-input"
                    />
                  </div>
                  {parteError && <p className="parte-error">{parteError}</p>}
                  <BotonPrimario
                    text={guardandoParte ? 'Guardando...' : 'Registrar Parte'}
                    onClick={() => void handleAddParte()}
                  />
                </div>
              </div>
            </section>
          )}
        </div>

        {/* ─── Columna Derecha ─── */}
        <div className="detalle-sidebar">
          {/* Historial de estados */}
          <section className="detalle-card">
            <h2>Historial de Estados</h2>
            {historial.length === 0 ? (
              <p className="sin-datos">Sin cambios de estado registrados.</p>
            ) : (
              <ul className="historial-lista">
                {historial.map((h) => (
                  <li key={h.id} className="historial-item">
                    <span className="historial-fecha">{new Date(h.fechaCambio).toLocaleString()}</span>
                    <div className="historial-cambio">
                      <EstadoBadge estado={h.estadoAnterior} />
                      <span className="historial-arrow">→</span>
                      <EstadoBadge estado={h.estadoNuevo} />
                    </div>
                    {h.nombreUsuario && (
                      <span className="historial-usuario">por {h.nombreUsuario}</span>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </section>

          {/* Comentarios */}
          <section className="detalle-card">
            <h2>Comentarios</h2>
            <div className="comentarios-lista">
              {comentarios.length === 0 ? (
                <p className="sin-datos">Sin comentarios aún.</p>
              ) : (
                comentarios.map((c) => (
                  <div key={c.id} className={`comentario-item ${c.esInterno ? 'comentario-interno' : ''}`}>
                    <div className="comentario-meta">
                      <strong>{c.nombreUsuario}</strong>
                      {c.esInterno && <span className="badge-interno">🔒 Nota Interna</span>}
                      <span className="comentario-fecha">
                        {c.fecha ? new Date(c.fecha).toLocaleString() : 'Sin fecha'}
                      </span>
                    </div>
                    <p className="comentario-texto">{c.texto}</p>
                  </div>
                ))
              )}
            </div>

            <div className="nuevo-comentario">
              <TextArea
                placeHolder="Escribe un comentario..."
                value={nuevoComentario}
                onChange={(e) => setNuevoComentario(e.target.value)}
                minRows={3}
              />
              {isAdminOrTech && (
                <label className="interno-toggle">
                  <input
                    type="checkbox"
                    checked={comentarioEsInterno}
                    onChange={(e) => setComentarioEsInterno(e.target.checked)}
                    id="esInterno"
                  />
                  <span>🔒 Nota interna (solo técnicos/admin)</span>
                </label>
              )}
              <BotonPrimario text="Añadir Comentario" onClick={() => void handleAddComment()} />
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}
