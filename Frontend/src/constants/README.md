# Constants - Constantes de la Aplicación

Valores constantes y configuraciones estáticas.

## Estructura

```
constants/
├── routes.constants.ts    # Rutas de la aplicación
├── api.constants.ts       # URLs y endpoints de API
├── app.constants.ts       # Configuración general
└── index.ts              # Barrel export
```

## Convenciones

- Nombres en UPPER_SNAKE_CASE
- Agrupar por categoría
- Exportar como const
- Usar Object.freeze para inmutabilidad

## Ejemplo

```typescript
// routes.constants.ts
export const ROUTES = {
  HOME: '/',
  LOGIN: '/login',
  REGISTER: '/register',
  INCIDENCIAS: '/incidencias',
  INCIDENCIA_DETAIL: '/incidencias/:id',
  CREATE_INCIDENCIA: '/incidencias/nueva',
  USUARIOS: '/usuarios',
  PERFIL: '/perfil',
  ADMIN: '/admin',
  NOT_FOUND: '/404',
} as const;

// api.constants.ts
export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000/api';

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    LOGOUT: '/auth/logout',
    REFRESH: '/auth/refresh',
  },
  INCIDENCIAS: {
    BASE: '/incidencias',
    BY_ID: (id: string) => \`/incidencias/\${id}\`,
    BY_USUARIO: (userId: string) => \`/incidencias/usuario/\${userId}\`,
  },
  USUARIOS: {
    BASE: '/usuarios',
    BY_ID: (id: string) => \`/usuarios/\${id}\`,
    PROFILE: '/usuarios/perfil',
  },
} as const;

// app.constants.ts
export const APP_NAME = 'Sistema de Gestión de Incidencias';
export const APP_VERSION = '1.0.0';

export const PAGINATION = {
  DEFAULT_PAGE: 1,
  DEFAULT_LIMIT: 10,
  MAX_LIMIT: 100,
} as const;

export const DATE_FORMATS = {
  SHORT: 'dd/MM/yyyy',
  LONG: 'dd/MM/yyyy HH:mm',
  TIME: 'HH:mm',
} as const;
```
