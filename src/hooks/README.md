# Hooks - Custom React Hooks

Esta carpeta contiene hooks personalizados reutilizables.

## Estructura

```
hooks/
├── useAuth.ts              # Autenticación
├── useIncidencias.ts       # Gestión de incidencias
├── useUsuarios.ts          # Gestión de usuarios
├── useForm.ts              # Manejo de formularios
├── useDebounce.ts          # Debounce para búsquedas
├── useLocalStorage.ts      # Persistencia local
└── useFetch.ts             # Peticiones HTTP genéricas
```

## Convenciones

- Prefijo `use` obligatorio
- Nombres en camelCase
- Un hook por archivo
- Exportar como named export
- Incluir tipos TypeScript

## Ejemplo

```typescript
import { useState, useEffect } from 'react';
import type { Incidencia } from '@/types/incidencia.types';
import { incidenciasService } from '@/services/api/incidencias.service';

export const useIncidencias = () => {
  const [incidencias, setIncidencias] = useState<Incidencia[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const fetchIncidencias = async () => {
    setLoading(true);
    try {
      const data = await incidenciasService.getAll();
      setIncidencias(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error desconocido');
    } finally {
      setLoading(false);
    }
  };

  return { incidencias, loading, error, fetchIncidencias };
};
```
