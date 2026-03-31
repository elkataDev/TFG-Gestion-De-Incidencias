import { useEffect, useState } from 'react';
import SelectAutoWidth from '@/components/common/Select/Select';
import { apiJson } from '@/services/api/apiService';

interface Aula {
  id: number;
  nombre: string;
}

interface SelectAulasProps {
  value?: string;
  onChange: (aulaValue?: string) => void;
  returnField?: 'id' | 'name';
}

export const SelectAulas = ({ value, onChange, returnField = 'id' }: SelectAulasProps) => {
  const [aulas, setAulas] = useState<Aula[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAulas = async () => {
      try {
        const data = (await apiJson('/aulas')) as Aula[];
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
        label: `${String(aula.id)} - ${aula.nombre}`,
      }))}
      value={
        value
          ? (() => {
              const match =
                returnField === 'name'
                  ? aulas.find((a) => a.nombre === value)
                  : aulas.find((a) => String(a.id) === value);
              return match ? `${String(match.id)} - ${match.nombre}` : value;
            })()
          : ''
      }
      onChange={(val) => {
        const parts = val?.split(' - ');
        const result = returnField === 'name' ? parts?.[1] : parts?.[0];
        onChange(result);
      }}
      disabled={loading}
    />
  );
};
