import { useState } from 'react';
import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import TextArea from '@/components/common/TextArea/TextArea';
import SelectAutoWidth from '@/components/common/Select/Select';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Input } from '@/components/common/Input/Input';
import { apiFetch, apiJson } from '@/services/api/apiService';
import './PagNuevaAveria.css';

interface FormDataType {
  titulo: string;
  descripcion: string;
  categoria: string;
  activoId: string;
}

interface ActivoResumen {
  id: number;
  nombre: string;
  codigoQR: string;
  categoria?: string;
  estado?: string;
  aulaId?: number | null;
  nombreAula?: string;
}

export default function PagNuevaAveria() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  // Estado del formulario
  const [form, setForm] = useState<FormDataType>({
    titulo: '',
    descripcion: '',
    categoria: '',
    activoId: '',
  });

  // Archivo adjunto
  const [file, setFile] = useState<File | null>(null);

  // Estado para mostrar errores
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [activos, setActivos] = useState<ActivoResumen[]>([]);
  const [activoSeleccionado, setActivoSeleccionado] = useState<ActivoResumen | null>(null);

  useEffect(() => {
    void (async () => {
      try {
        const data = (await apiJson('/inventario')) as ActivoResumen[];
        setActivos(Array.isArray(data) ? data : []);
      } catch (error) {
        setErrorMsg(error instanceof Error ? error.message : 'No se pudieron cargar los activos');
      }
    })();
  }, []);

  useEffect(() => {
    const activoIdRaw = searchParams.get('activoId');
    if (!activoIdRaw) return;

    const activoId = Number(activoIdRaw);
    if (!Number.isFinite(activoId)) {
      setErrorMsg('activoId no válido en la URL');
      return;
    }

    void (async () => {
      try {
        const data = (await apiJson(`/inventario/${activoId}`)) as ActivoResumen;
        setActivoSeleccionado(data);
        setForm((prev) => ({
          ...prev,
          activoId: String(data.id),
        }));
      } catch (error) {
        setErrorMsg(error instanceof Error ? error.message : 'No se pudo cargar el activo seleccionado');
      }
    })();
  }, [searchParams]);

  useEffect(() => {
    if (!form.activoId) {
      setActivoSeleccionado(null);
      return;
    }

    const selected = activos.find((activo) => String(activo.id) === form.activoId) ?? null;
    if (!selected) return;

    setActivoSeleccionado(selected);
  }, [activos, form.activoId]);

  const handleChange = (field: keyof FormDataType, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setFile(e.target.files[0] ?? null);
    }
  };

  const handleSubmit = async () => {
    setErrorMsg(null);
    setSuccessMsg(null);

    if (!form.titulo.trim()) {
      setErrorMsg('El título es obligatorio');
      return;
    }

    if (!form.descripcion.trim()) {
      setErrorMsg('La descripción es obligatoria');
      return;
    }

    if (!form.categoria) {
      setErrorMsg('Selecciona una categoría');
      return;
    }

    if (!form.activoId) {
      setErrorMsg('Selecciona el activo afectado');
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      setErrorMsg('No autorizado, por favor haz login.');
      return;
    }

    try {
      const data = new FormData();
      data.append(
        'incidencia',
        new Blob(
          [
            JSON.stringify({
              titulo: form.titulo,
              descripcion: form.descripcion,
              categoria: form.categoria ? form.categoria : 'HARDWARE',
              activoId: Number(form.activoId),
            }),
          ],
          { type: 'application/json' }
        )
      );

      if (file) {
        data.append('file', file);
      }

      await apiFetch('/incidencias/reportar', {
        method: 'POST',
        body: data,
      });

      setSuccessMsg('Avería creada correctamente');
      setTimeout(() => void navigate('/averias'), 1500);
    } catch (error) {
      // Error de conexión
      setErrorMsg(error instanceof Error ? error.message : 'Error de conexión con la API');
    }
  };

  return (
    <div className="pag-nueva-averia">
      <div className="report-container">
        <h1>Reportar Nueva Avería</h1>
        <p>Complete el siguiente formulario para reportar un problema técnico</p>

        <span className="select-container">
          <h3>Activo afectado *</h3>
          <select
            className="activo-select"
            value={form.activoId}
            onChange={(e) => handleChange('activoId', e.target.value)}
          >
            <option value="">Selecciona el activo averiado</option>
            {activos.map((activo) => (
              <option key={activo.id} value={activo.id}>
                {activo.nombre} · {activo.codigoQR}{activo.nombreAula ? ` · ${activo.nombreAula}` : ''}
              </option>
            ))}
          </select>
        </span>

        {activoSeleccionado && (
          <div className="activo-seleccionado-card">
            <strong>Activo seleccionado:</strong>
            <span>{activoSeleccionado.nombre} ({activoSeleccionado.codigoQR})</span>
            <small>Ubicación: {activoSeleccionado.nombreAula || 'Sin aula asignada'}</small>
          </div>
        )}

        <span style={{ marginBottom: '1rem', display: 'block' }}>
          <h3>Título de la incidencia</h3>
          <Input
            name="titulo"
            label="Ej: Proyector no enciende"
            type="text"
            value={form.titulo}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              handleChange('titulo', e.target.value)
            }
          />
        </span>

        <TextArea
          placeHolder="Describa la averia en detalle..."
          minRows={6}
          value={form.descripcion}
          onChange={(e) => handleChange('descripcion', e.target.value)}
        />

        <span className="select-container">
          <h3>Tipo de problema</h3>
          <SelectAutoWidth
            inputText="Seleccionar Tipo"
            options={[{ label: 'HARDWARE' }, { label: 'SOFTWARE' }, { label: 'RED' }]}
            value={form.categoria}
            onChange={(value: string) => handleChange('categoria', value)}
          />
        </span>

        <span className="file-upload-container" style={{ margin: '1rem 0', display: 'block' }}>
          <h3>Archivo Adjunto (Opcional)</h3>
          <label className="file-input-label">
            <input className="file-input" type="file" onChange={handleFileChange} />
            <span>Seleccionar archivo</span>
          </label>
          <p className="selected-file-name">
            {file ? `Archivo seleccionado: ${file.name}` : 'Ningún archivo seleccionado'}
          </p>
        </span>

        {errorMsg && <p style={{ color: 'red' }}>{errorMsg}</p>}
        {successMsg && <p style={{ color: 'green' }}>{successMsg}</p>}

        <span className="button-container">
          <BotonPrimario onClick={() => void navigate('/')} text="Volver al inicio" />
          <BotonPrimario onClick={() => void handleSubmit()} text="Enviar" />
        </span>
      </div>
    </div>
  );
}
