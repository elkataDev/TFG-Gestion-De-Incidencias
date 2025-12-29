import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import './PagEditarActivo.css';

type Activo = {
  id: string;
  nombre: string;
  categoria: string;
  ubicacion: string;
};

export default function EditarActivo() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [activo, setActivo] = useState<Activo | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;

    fetch(`/api/activos/${id}`)
      .then((res) => res.json() as Promise<Activo>)
      .then((data) => {
        setActivo(data);
        setLoading(false);
      })
      .catch(() => {
        alert('Error al cargar el activo');
        setLoading(false);
      });
  }, [id]);

  // 🔹 Manejar cambios
  const handleChange = (field: keyof Activo, value: string) => {
    if (!activo) return;
    setActivo({ ...activo, [field]: value });
  };

  // 🔹 Guardar cambios
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    fetch(`/api/activos/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(activo),
    })
      .then(() => {
        alert('Activo actualizado correctamente');
        void navigate('/activos');
      })
      .catch(() => alert('Error al guardar'));
  };

  if (loading) return <p>Cargando activo...</p>;
  if (!activo) return <p>No se encontró el activo</p>;

  return (
    <div className="form-container">
      <h1>Editar Activo</h1>

      <form onSubmit={handleSubmit} className="form-activo">
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

        {/* Categoría */}
        <label>
          Categoría
          <select
            value={activo.categoria}
            onChange={(e) => handleChange('categoria', e.target.value)}
            required
          >
            <option value="">Seleccionar</option>
            <option value="Computadora">Computadora</option>
            <option value="Impresora">Impresora</option>
            <option value="Proyector">Proyector</option>
            <option value="Monitor">Monitor</option>
            <option value="Red">Red</option>
            <option value="Servidor">Servidor</option>
            <option value="Periférico">Periférico</option>
            <option value="Seguridad">Seguridad</option>
          </select>
        </label>

        {/* Ubicación */}
        <label>
          Ubicación
          <input
            type="text"
            value={activo.ubicacion}
            onChange={(e) => handleChange('ubicacion', e.target.value)}
            required
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
