import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BotonSecundario from '@/components/common/BotonSecundario/BotonSecundario';
import { Input } from '@/components/common/Input/Input';
import TextArea from '@/components/common/TextArea/TextArea';
import SelectAutoWidth from '@/components/common/Select/Select';
import { SelectAulas } from '@/components/common/SelectsBD/SelectAula';
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
    <div className="pag-nuevo-activo">
      <div className="report-container">
        <h1>Nuevo Activo</h1>
        <p>Añada un nuevo activo al inventario del sistema</p>

        <form onSubmit={(e) => void handleSubmit(e)}>
          <span style={{ marginBottom: '1rem', display: 'block' }}>
            <h3>Nombre *</h3>
            <Input
              name="nombre"
              label="Ej: PC-101"
              type="text"
              value={form.nombre}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                handleChange('nombre', e.target.value)
              }
              required
            />
          </span>

          <h3>Descripción</h3>
          <TextArea
            placeHolder="Descripción detallada del activo"
            minRows={4}
            value={form.descripcion}
            onChange={(e) => handleChange('descripcion', e.target.value)}
          />

          <span style={{ marginBottom: '1rem', display: 'block' }}>
            <h3>Código QR</h3>
            <Input
              name="codigoQR"
              label="Código QR único del activo"
              type="text"
              value={form.codigoQR}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                handleChange('codigoQR', e.target.value)
              }
            />
          </span>

          <span className="select-container">
            <h3>Categoría *</h3>
            <SelectAutoWidth
              inputText="Seleccionar Categoría"
              options={[
                { label: 'COMPUTADORA' },
                { label: 'IMPRESORA' },
                { label: 'PROYECTOR' },
                { label: 'MONITOR' },
                { label: 'RED' },
                { label: 'SERVIDOR' },
                { label: 'PERIFERICO' },
                { label: 'SEGURIDAD' },
              ]}
              value={form.categoria}
              onChange={(value: string) => handleChange('categoria', value as CategoriaInventario)}
            />
          </span>

          <span className="select-container">
            <h3>Estado *</h3>
            <SelectAutoWidth
              inputText="Seleccionar Estado"
              options={[
                { label: 'DISPONIBLE' },
                { label: 'EN_USO' },
                { label: 'DANADO' },
              ]}
              value={form.estado}
              onChange={(value: string) => handleChange('estado', value as EstadoInventario)}
            />
          </span>

          <span className="select-container">
            <h3>Aula (Opcional)</h3>
            <SelectAulas
              value={form.aulaId}
              onChange={(value) => handleChange('aulaId', value ?? '')}
            />
          </span>

          {successMsg && <p style={{ color: 'green', margin: '10px 0' }}>{successMsg}</p>}
          {errorMsg && <p style={{ color: 'red', margin: '10px 0' }}>{errorMsg}</p>}

          <div className="button-container">
            <BotonSecundario text="Cancelar" onClick={() => void navigate(-1)} />
            <button type="submit" style={{ display: 'none' }} id="hidden-submit" />
            <BotonPrimario text={loading ? 'Guardando...' : 'Crear Activo'} onClick={() => document.getElementById('hidden-submit')?.click()} />
          </div>
        </form>
      </div>
    </div>
  );
}
