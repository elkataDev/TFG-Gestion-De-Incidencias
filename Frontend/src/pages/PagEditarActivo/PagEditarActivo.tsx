import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { apiFetch } from '@/services/api/apiService';
import './PagEditarActivo.css';

/* ===================== TIPOS ===================== */

type Categoria =
  | 'COMPUTADORA'
  | 'IMPRESORA'
  | 'PROYECTOR'
  | 'MONITOR'
  | 'RED'
  | 'SERVIDOR'
  | 'PERIFERICO'
  | 'SEGURIDAD';

interface Activo {
  id: number;
  nombre: string;
  descripcion: string;
  codigoQR: string;
  categoria: Categoria;
  estado: string;
  fechaIngreso: string;
  aulaId: number;
}

/* ===================== COMPONENTE ===================== */

export default function EditarActivo() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [activo, setActivo] = useState<Activo | null>(null);
  const [loading, setLoading] = useState(true);

  /* ===================== CARGAR ACTIVO ===================== */
  useEffect(() => {
    if (!id) return;

    apiFetch(`/inventario/${id}`)
      .then(async (res) => {
        const text = await res.text();

        try {
          const data = JSON.parse(text);

          const activoData: Activo = {
            id: data.id,
            nombre: data.nombre,
            descripcion: data.descripcion,
            codigoQR: data.codigoQR,
            categoria: data.categoria, // 👈 MAYÚSCULAS
            estado: data.estado,
            fechaIngreso: data.fechaIngreso.slice(0, 10),
            aulaId: data.aulaId,
          };

          setActivo(activoData);
        } catch {
          alert('Error: la respuesta del servidor no es válida');
        }

        setLoading(false);
      })
      .catch((err: unknown) => {
        alert(err instanceof Error ? err.message : 'Error al cargar el activo');
        setLoading(false);
      });
  }, [id]);

  /* ===================== HANDLERS ===================== */

  const handleChange = (field: keyof Activo, value: string | number) => {
    if (!activo) return;

    setActivo({ ...activo, [field]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!activo) return;

    try {
      await apiFetch(`/inventario/${id}`, {
        method: 'PUT',
        body: JSON.stringify(activo),
      });

      alert('Activo actualizado correctamente');
      void navigate('/activos');
    } catch (err: unknown) {
      alert(err instanceof Error ? err.message : 'Error de conexión');
    }
  };

  /* ===================== RENDER ===================== */

  if (loading) return <p>Cargando activo...</p>;
  if (!activo) return <p>No se encontró el activo</p>;

  return (
    <div className="form-container">
      <h1>Editar Activo</h1>

      <form onSubmit={(e) => void handleSubmit(e)} className="form-activo">
        {/* Nombre */}
        <label>
          Nombre
          <input
            type="text"
            value={activo.nombre}
            onChange={(e) => handleChange('nombre', e.target.value)}
            required
          />
        </label>

        {/* Descripción */}
        <label>
          Descripción
          <textarea
            value={activo.descripcion}
            onChange={(e) => handleChange('descripcion', e.target.value)}
            rows={4}
            required
          />
        </label>

        {/* Código QR */}
        <label>
          Código QR
          <input
            type="text"
            value={activo.codigoQR}
            onChange={(e) => handleChange('codigoQR', e.target.value)}
            required
          />
        </label>

        {/* Categoría */}
        <label>
          Categoría
          <select
            value={activo.categoria}
            onChange={(e) => handleChange('categoria', e.target.value as Categoria)}
            required
          >
            <option value="COMPUTADORA">Computadora</option>
            <option value="IMPRESORA">Impresora</option>
            <option value="PROYECTOR">Proyector</option>
            <option value="MONITOR">Monitor</option>
            <option value="RED">Red</option>
            <option value="SERVIDOR">Servidor</option>
            <option value="PERIFERICO">Periférico</option>
            <option value="SEGURIDAD">Seguridad</option>
          </select>
        </label>

        {/* Estado */}
        <label>
          Estado
          <select
            value={activo.estado}
            onChange={(e) => handleChange('estado', e.target.value)}
            required
          >
            <option value="DISPONIBLE">DISPONIBLE</option>
            <option value="EN_USO">EN_USO</option>
            <option value="DANADO">DANADO</option>
          </select>
        </label>

        {/* Aula */}
        <label>
          Aula ID
          <input
            type="number"
            value={activo.aulaId}
            onChange={(e) => handleChange('aulaId', Number(e.target.value))}
          />
        </label>

        {/* Botones */}
        <div className="form-actions">
          <BotonPrimario text="Guardar cambios" type="submit" />
          <button type="button" onClick={() => void navigate(-1)}>
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
}
