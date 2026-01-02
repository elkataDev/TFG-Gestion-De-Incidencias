import { useEffect, useState } from 'react';
import SelectAutoWidth from '@/components/common/Select/Select';

interface Aula {
  id: number;
  nombre: string;
}

interface SelectAulasProps {
  value?: string; // <-- ahora string
  onChange: (nombre?: string) => void; // <-- ahora string
}

export const SelectAulas = ({ value, onChange }: SelectAulasProps) => {
  const [aulas, setAulas] = useState<Aula[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAulas = async () => {
      try {
        const response = await fetch('http://localhost:5555/api/aulas', {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });

        if (!response.ok) throw new Error('Error al cargar aulas');

        const data: Aula[] = await response.json();
        setAulas(data);
      } catch (error) {
        console.error('Error al cargar aulas', error);
      } finally {
        setLoading(false);
      }
    };

    void fetchAulas();
  }, []);

  return (
    <SelectAutoWidth
      inputText="Aula"
      options={aulas.map((aula) => ({
        label: aula.nombre, // solo label
      }))}
      value={value} // ahora es string
      onChange={(val) => onChange(val)} // val ya es string | undefined
      disabled={loading}
    />
  );
};
