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
  tecnicoAsignadoId?: number | null;
  nombreTecnicoAsignado?: string;
}

interface Comentario {
  id: number;
  texto: string;
  fecha: string;
  nombreUsuario: string;
  esInterna?: boolean;
}

interface UsuarioSistema {
  id: number;
  nombreUsuario: string;
  rol: 'USUARIO' | 'TECNICO' | 'ADMIN';
}

interface ParteTrabajo {
  id: number;
  minutos: number;
  descripcion: string;
  piezasUsadas?: string;
  coste?: number;
  fechaParte: string;
  nombreTecnico: string;
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
  const [tecnicos, setTecnicos] = useState<UsuarioSistema[]>([]);
  const [tecnicoId, setTecnicoId] = useState<string>('');
  const [esInterna, setEsInterna] = useState(false);
  const [partes, setPartes] = useState<ParteTrabajo[]>([]);
  const [minutosParte, setMinutosParte] = useState('');
  const [descripcionParte, setDescripcionParte] = useState('');
  const [piezasParte, setPiezasParte] = useState('');
  const [costeParte, setCosteParte] = useState('');

  const role = localStorage.getItem('role') ?? 'USUARIO';
  const isAdminOrTech = role === 'ADMIN' || role === 'TECNICO';

  const fetchIncidencia = async () => {
    try {
      const [resInc, resCom, resHist, resPartes] = await Promise.all([
        apiJson(`/incidencias/${id}`),
        apiJson(`/incidencias/${id}/comentarios`),
        apiJson(`/incidencias/${id}/historial`),
        apiJson(`/incidencias/${id}/partes`),
      ]);
      setIncidencia(resInc as Incidencia);
      setComentarios(resCom as Comentario[]);
      setHistorial(resHist as Historial[]);
      setPartes(Array.isArray(resPartes) ? (resPartes as ParteTrabajo[]) : []);
      const tecnicoAsignado = resInc as Incidencia;
      setTecnicoId(
        tecnicoAsignado.tecnicoAsignadoId ? String(tecnicoAsignado.tecnicoAsignadoId) : ''
      );
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

  useEffect(() => {
    if (!isAdminOrTech) return;
    void apiJson('/usuarios')
      .then((data) => {
        const users = Array.isArray(data) ? (data as UsuarioSistema[]) : [];
        setTecnicos(users.filter((u) => u.rol === 'TECNICO' || u.rol === 'ADMIN'));
      })
      .catch(console.error);
  }, [isAdminOrTech]);

  const handleAddComment = async () => {
    if (!nuevoComentario.trim()) return;
    try {
      await apiJson(`/incidencias/${id}/comentarios`, {
        method: 'POST',
        body: JSON.stringify({ texto: nuevoComentario, esInterna }),
      });
      setNuevoComentario('');
      setEsInterna(false);
      void fetchIncidencia(); // refresh
    } catch (err) {
      console.error('Error posting comment', err);
    }
  };

  const handleAsignarTecnico = async () => {
    if (!tecnicoId) return;
    try {
      await apiJson(`/incidencias/${id}/asignar-tecnico`, {
        method: 'PATCH',
        body: JSON.stringify({ tecnicoId: Number(tecnicoId) }),
      });
      void fetchIncidencia();
    } catch (err) {
      console.error('Error asignando técnico', err);
    }
  };

  const handleAddParte = async () => {
    if (!minutosParte || !descripcionParte.trim()) return;
    try {
      await apiJson(`/incidencias/${id}/partes`, {
        method: 'POST',
        body: JSON.stringify({
          minutos: Number(minutosParte),
          descripcion: descripcionParte,
          piezasUsadas: piezasParte,
          coste: costeParte || null,
        }),
      });
      setMinutosParte('');
      setDescripcionParte('');
      setPiezasParte('');
      setCosteParte('');
      void fetchIncidencia();
    } catch (err) {
      console.error('Error creando parte', err);
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
            <strong>Técnico asignado:</strong> {incidencia.nombreTecnicoAsignado || 'Sin asignar'}
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

          {isAdminOrTech && (
            <div className="estado-updater">
              <h3>Actualizar Estado</h3>
              <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
                <SelectAutoWidth
                  inputText="Nuevo Estado"
                  options={[
                    { label: 'ABIERTO' },
                    { label: 'EN_PROGRESO' },
                    { label: 'EN_ESPERA' },
                    { label: 'RESUELTO' },
                    { label: 'CERRADO' },
                    { label: 'REABIERTO' },
                  ]}
                  value={nuevoEstado}
                  onChange={setNuevoEstado}
                />
                <BotonPrimario text="Actualizar" onClick={() => void handleUpdateEstado()} />
              </div>
            </div>
          )}

          {isAdminOrTech && (
            <div className="estado-updater">
              <h3>Asignar Técnico</h3>
              <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
                <SelectAutoWidth
                  inputText="Técnico"
                  options={tecnicos.map((t) => ({ label: t.nombreUsuario }))}
                  value={tecnicos.find((t) => String(t.id) === tecnicoId)?.nombreUsuario ?? ''}
                  onChange={(nombre) => {
                    const selected = tecnicos.find((t) => t.nombreUsuario === nombre);
                    setTecnicoId(selected ? String(selected.id) : '');
                  }}
                />
                <BotonPrimario text="Asignar" onClick={() => void handleAsignarTecnico()} />
              </div>
            </div>
          )}
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
                {c.esInterna && <p style={{ fontSize: '0.8rem', color: '#b45309' }}>Nota interna</p>}
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
            {isAdminOrTech && (
              <label style={{ display: 'flex', gap: '8px', margin: '8px 0' }}>
                <input
                  type="checkbox"
                  checked={esInterna}
                  onChange={(e) => setEsInterna(e.target.checked)}
                />
                Guardar como nota interna
              </label>
            )}
            <BotonPrimario text="Añadir Comentario" onClick={() => void handleAddComment()} />
          </div>
        </div>

        <div className="partes-section">
          <div className="partes-header">
            <div>
              <span className="section-eyebrow">Trabajo técnico</span>
              <h2>Partes de trabajo</h2>
            </div>
            <span className="partes-count">{partes.length} partes</span>
          </div>

          {partes.length === 0 ? (
            <div className="empty-partes">
              <strong>No hay partes registrados</strong>
              <p>Cuando un técnico registre trabajo, aparecerá aquí con tiempo, piezas y coste.</p>
            </div>
          ) : (
            <div className="partes-list">
              {partes.map((p) => (
                <article key={p.id} className="parte-card">
                  <div className="parte-card-header">
                    <div>
                      <strong>{p.nombreTecnico || 'Técnico'}</strong>
                      <span>{new Date(p.fechaParte).toLocaleString()}</span>
                    </div>
                    <span className="parte-time">{p.minutos} min</span>
                  </div>
                  <p className="parte-description">{p.descripcion}</p>
                  <div className="parte-meta-grid">
                    <span>
                      <strong>Piezas</strong>
                      {p.piezasUsadas || 'Sin piezas'}
                    </span>
                    <span>
                      <strong>Coste</strong>
                      {p.coste ? `${p.coste}€` : 'Sin coste'}
                    </span>
                  </div>
                </article>
              ))}
            </div>
          )}

          {isAdminOrTech && (
            <div className="parte-form">
              <h3>Nuevo parte</h3>
              <div className="parte-form-grid">
                <label>
                  <span>Minutos invertidos *</span>
                  <input
                    placeholder="Ej: 45"
                    type="number"
                    min="1"
                    value={minutosParte}
                    onChange={(e) => setMinutosParte(e.target.value)}
                  />
                </label>
                <label>
                  <span>Coste opcional</span>
                  <input
                    placeholder="Ej: 12.50"
                    type="number"
                    step="0.01"
                    min="0"
                    value={costeParte}
                    onChange={(e) => setCosteParte(e.target.value)}
                  />
                </label>
              </div>
              <TextArea
                placeHolder="Descripción del trabajo realizado"
                value={descripcionParte}
                onChange={(e) => setDescripcionParte(e.target.value)}
                minRows={2}
              />
              <label className="parte-full-field">
                <span>Piezas usadas</span>
                <input
                  placeholder="Ej: cable HDMI, lámpara proyector..."
                  value={piezasParte}
                  onChange={(e) => setPiezasParte(e.target.value)}
                />
              </label>
              <div className="parte-form-actions">
                <BotonPrimario text="Guardar parte" onClick={() => void handleAddParte()} />
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
