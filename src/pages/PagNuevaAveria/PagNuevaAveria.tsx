import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TextArea from '@/components/common/TextArea/TextArea';
import SelectAutoWidth from '@/components/common/Select/Select';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import './PagNuevaAveria.css';

interface FormData {
  descripcion: string;
  tipoActivo: string;
  ubicacion: string;
}

export default function PagNuevaAveria() {
  const navigate = useNavigate();

  // Estado del formulario
  const [form, setForm] = useState<FormData>({
    descripcion: '',
    tipoActivo: '',
    ubicacion: '',
  });

  // Estado para mostrar errores
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  const handleChange = (field: keyof FormData, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async () => {
    setErrorMsg(null);
    setSuccessMsg(null);

    const token = localStorage.getItem('token');
    if (!token) {
      setErrorMsg('No autorizado, por favor haz login.');
      return;
    }

    try {
      const response = await fetch('https://tu-api.com/averias', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        const errorText = await response.text();
        setErrorMsg(`Error al crear avería: ${errorText}`);
        return;
      }

      setSuccessMsg('Avería creada correctamente');
      // No redirigimos automáticamente, solo mostramos mensaje
    } catch (error) {
      console.error('Error de conexión:', error);
      setErrorMsg('Error de conexión con la API');
    }
  };

  return (
    <div className="pag-nueva-averia">
      <div className="report-container">
        <h1>Reportar Nueva Averia</h1>
        <p>Complete el siguiente formulario para reportar un problema técnico</p>

        <TextArea
          placeHolder="Describa la averia en detalle..."
          minRows={10}
          value={form.descripcion}
          onChange={(e) => handleChange('descripcion', e.target.value)}
        />

        <span className="select-container">
          <h3>Tipo de Archivo</h3>
          <SelectAutoWidth
            inputText="Seleccionar Tipo de Activo"
            options={[{ label: 'Activo1' }, { label: 'Activo2' }]}
            value={form.tipoActivo}
            onChange={(value: string) => handleChange('tipoActivo', value)}
          />
        </span>

        <span className="select-container">
          <h3>Ubicacion</h3>
          <SelectAutoWidth
            inputText="Seleccionar Ubicación"
            options={[{ label: 'Ubicacion1' }, { label: 'Ubicacion2' }]}
            value={form.ubicacion}
            onChange={(value: string) => handleChange('ubicacion', value)}
          />
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
