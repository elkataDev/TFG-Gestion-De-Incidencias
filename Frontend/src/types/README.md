# Types - Definiciones de Tipos TypeScript

Tipos, interfaces y enums compartidos en la aplicación.

## Estructura

```
types/
├── incidencia.types.ts     # Tipos de incidencias
├── usuario.types.ts        # Tipos de usuarios
├── auth.types.ts           # Tipos de autenticación
├── common.types.ts         # Tipos comunes/compartidos
└── index.ts               # Barrel export
```

## Convenciones

- Sufijo `.types.ts`
- Usar `interface` para objetos
- Usar `type` para uniones/intersecciones
- Prefijo `I` opcional para interfaces
- Sufijo `Dto` para Data Transfer Objects
- Sufijo `Response` para respuestas de API

## Ejemplo

```typescript
// incidencia.types.ts

export enum EstadoIncidencia {
  ABIERTA = 'ABIERTA',
  EN_PROGRESO = 'EN_PROGRESO',
  RESUELTA = 'RESUELTA',
  CERRADA = 'CERRADA',
}

export enum PrioridadIncidencia {
  BAJA = 'BAJA',
  MEDIA = 'MEDIA',
  ALTA = 'ALTA',
  CRITICA = 'CRITICA',
}

export interface Incidencia {
  id: string;
  titulo: string;
  descripcion: string;
  estado: EstadoIncidencia;
  prioridad: PrioridadIncidencia;
  usuarioId: string;
  asignadoA?: string;
  fechaCreacion: Date;
  fechaActualizacion: Date;
  fechaResolucion?: Date;
}

export interface CreateIncidenciaDto {
  titulo: string;
  descripcion: string;
  prioridad: PrioridadIncidencia;
}

export interface UpdateIncidenciaDto {
  titulo?: string;
  descripcion?: string;
  estado?: EstadoIncidencia;
  prioridad?: PrioridadIncidencia;
  asignadoA?: string;
}

export interface IncidenciaResponse {
  data: Incidencia;
  message: string;
}

export interface IncidenciasResponse {
  data: Incidencia[];
  total: number;
  page: number;
  limit: number;
}
```
