import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { apiJson } from '@/services/api/apiService';
import './PagNuevoActivo.css';

/* ===================== TIPOS ===================== */

type EstadoInventario = 'DISPONIBLE' | 'EN_USO' | 'DANADO';

type CategoriaInventario =
  | 'COMPUTADORA'
  | 'IMPRESORA'
  | 'PROYECTOR'
  | 'MONITOR'
  | 'RED'
  | 'SERVIDOR'
  | 'PERIFERICO'
  | 'SEGURIDAD';

interface NuevoActivoForm {
  nombre: string;
  descripcion: string;
  codigoQR: string;
  estado: EstadoInventario;
  categoria: CategoriaInventario;
  aulaId: string;
}

const INITIAL_FORM: NuevoActivoForm = {
  nombre: '',
  descripcion: '',
  codigoQR: '',
  estado: 'DISPONIBLE',
  categoria: 'COMPUTADORA',
  aulaId: '',
};

/* ===================== COMPONENTE ===================== */

export default function PagNuevoActivo() {
  const navigate = useNavigate();
  const [form, setForm] = useState<NuevoActivoForm>(INITIAL_FORM);
  const [loading, setLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  /* ===================== HANDLERS ===================== */

  const handleChange = (field: keyof NuevoActivoForm, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    const payload = {
      nombre: form.nombre,
      descripcion: form.descripcion,
      codigoQR: form.codigoQR || undefined,
      estado: form.estado,
      categoria: form.categoria,
      aulaId: form.aulaId ? Number(form.aulaId) : null,
    };

    try {
      await apiJson('/inventario', {
        method: 'POST',
        body: JSON.stringify(payload),
      });

      setSuccessMsg('Activo creado correctamente');
      setTimeout(() => void navigate('/inventario'), 1500);
    } catch (err: unknown) {
      console.error('Error al crear activo:', err);
      setErrorMsg(err instanceof Error ? err.message : 'Error al crear el activo');
    } finally {
      setLoading(false);
    }
  };

  /* ===================== RENDER ===================== */

  return (
    <div className="form-container">
      <h1>Nuevo Activo</h1>

      <form onSubmit={(e) => void handleSubmit(e)} className="form-activo">
        {/* Nombre */}
        <label>
          Nombre *
          <input
            type="text"
            value={form.nombre}
            onChange={(e) => handleChange('nombre', e.target.value)}
            required
            maxLength={100}
            placeholder="Ej: PC-101"
          />
        </label>

        {/* Descripción */}
        <label>
          Descripción
          <textarea
            value={form.descripcion}
            onChange={(e) => handleChange('descripcion', e.target.value)}
            rows={3}
            maxLength={255}
            placeholder="Descripción detallada del activo"
          />
        </label>

        {/* Código QR */}
        <label>
          Código QR
          <input
            type="text"
            value={form.codigoQR}
            onChange={(e) => handleChange('codigoQR', e.target.value)}
            maxLength={50}
            placeholder="Código QR único del activo"
          />
        </label>

        {/* Categoría */}
        <label>
          Categoría *
          <select
            value={form.categoria}
            onChange={(e) => handleChange('categoria', e.target.value as CategoriaInventario)}
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
          Estado *
          <select
            value={form.estado}
            onChange={(e) => handleChange('estado', e.target.value as EstadoInventario)}
            required
          >
            <option value="DISPONIBLE">Disponible</option>
            <option value="EN_USO">En uso</option>
            <option value="DANADO">Dañado</option>
          </select>
        </label>

        {/* Aula ID */}
        <label>
          Aula ID (opcional)
          <input
            type="number"
            value={form.aulaId}
            onChange={(e) => handleChange('aulaId', e.target.value)}
            min={1}
            placeholder="ID del aula (dejar vacío si está en almacén)"
          />
        </label>

        {successMsg && <p style={{ color: 'green' }}>{successMsg}</p>}
        {errorMsg && <p style={{ color: 'red' }}>{errorMsg}</p>}

        {/* Botones */}
        <div className="form-actions">
          <BotonPrimario text={loading ? 'Guardando...' : 'Crear Activo'} type="submit" />
          <button type="button" onClick={() => void navigate(-1)} disabled={loading}>
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
}
