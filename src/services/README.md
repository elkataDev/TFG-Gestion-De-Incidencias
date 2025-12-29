# Services - Servicios y Lógica de Negocio

Capa de servicios para comunicación con APIs y manejo de datos.

## Estructura

### 📁 `api/`

Servicios de comunicación con el backend.

```
api/
├── axios.config.ts         # Configuración de Axios
├── incidencias.service.ts  # CRUD de incidencias
├── usuarios.service.ts     # CRUD de usuarios
├── auth.service.ts         # Autenticación
└── index.ts               # Barrel export
```

### 📁 `storage/`

Servicios de almacenamiento local.

```
storage/
├── localStorage.service.ts # LocalStorage utils
├── sessionStorage.service.ts # SessionStorage utils
└── index.ts
```

## Convenciones

- Un servicio por entidad/módulo
- Sufijo `.service.ts`
- Exportar objeto con métodos
- Usar async/await
- Manejo de errores centralizado

## Ejemplo de Servicio API

```typescript
import axios from './axios.config';
import type { Incidencia, CreateIncidenciaDto } from '@/types/incidencia.types';

export const incidenciasService = {
  getAll: async (): Promise<Incidencia[]> => {
    const response = await axios.get('/incidencias');
    return response.data;
  },

  getById: async (id: string): Promise<Incidencia> => {
    const response = await axios.get(\`/incidencias/\${id}\`);
    return response.data;
  },

  create: async (data: CreateIncidenciaDto): Promise<Incidencia> => {
    const response = await axios.post('/incidencias', data);
    return response.data;
  },

  update: async (id: string, data: Partial<Incidencia>): Promise<Incidencia> => {
    const response = await axios.put(\`/incidencias/\${id}\`, data);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await axios.delete(\`/incidencias/\${id}\`);
  },
};
```
