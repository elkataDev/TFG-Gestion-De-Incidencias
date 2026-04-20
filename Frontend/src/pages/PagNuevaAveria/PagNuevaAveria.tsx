import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TextArea from '@/components/common/TextArea/TextArea';
import SelectAutoWidth from '@/components/common/Select/Select';
import { SelectAulas } from '@/components/common/SelectsBD/SelectAula';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import { Input } from '@/components/common/Input/Input';
import { apiFetch } from '@/services/api/apiService';
import './PagNuevaAveria.css';

interface FormDataType {
  titulo: string;
  descripcion: string;
  categoria: string;
  aulaId: string;
}

export default function PagNuevaAveria() {
  const navigate = useNavigate();

  // Estado del formulario
  const [form, setForm] = useState<FormDataType>({
    titulo: '',
    descripcion: '',
    categoria: '',
    aulaId: '',
  });

  // Archivo adjunto
  const [file, setFile] = useState<File | null>(null);

  // Estado para mostrar errores
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

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

    if (!form.categoria) {
      setErrorMsg('Selecciona una categoría');
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
              aulaId: form.aulaId ? Number(form.aulaId) : null,
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
          <h3>Categoría</h3>
          <SelectAutoWidth
            inputText="Seleccionar Categoría"
            options={[{ label: 'HARDWARE' }, { label: 'SOFTWARE' }, { label: 'RED' }]}
            value={form.categoria}
            onChange={(value: string) => handleChange('categoria', value)}
          />
        </span>

        <span className="select-container">
          <h3>Aula (Opcional)</h3>
          <SelectAulas
            value={form.aulaId}
            onChange={(value) => handleChange('aulaId', value ?? '')}
          />
        </span>

        <span className="file-upload-container" style={{ margin: '1rem 0', display: 'block' }}>
          <h3>Archivo Adjunto (Opcional)</h3>
          <input className="file-input" type="file" onChange={handleFileChange} />
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
