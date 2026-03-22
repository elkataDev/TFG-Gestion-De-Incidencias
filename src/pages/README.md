# Pages - Páginas de la Aplicación

Esta carpeta contiene las páginas principales de la aplicación (vistas/rutas).

## Estructura Recomendada

```
pages/
├── Home.tsx                    # Página principal/dashboard
├── Login.tsx                   # Página de login
├── Register.tsx                # Página de registro
├── NotFound.tsx                # Página 404
├── Incidencias/
│   ├── IncidenciasPage.tsx    # Lista de incidencias
│   ├── IncidenciaDetailPage.tsx # Detalle de una incidencia
│   ├── CreateIncidenciaPage.tsx # Crear nueva incidencia
│   └── EditIncidenciaPage.tsx   # Editar incidencia
├── Usuarios/
│   ├── UsuariosPage.tsx        # Lista de usuarios
│   ├── UsuarioProfilePage.tsx  # Perfil de usuario
│   └── EditProfilePage.tsx     # Editar perfil
└── Admin/
    ├── AdminDashboard.tsx      # Panel de administración
    └── SettingsPage.tsx        # Configuración
```

## Convenciones

- Cada página es un componente que representa una ruta
- Usar sufijo `Page` para claridad (ej: `IncidenciasPage.tsx`)
- Las páginas orquestan componentes de `components/`
- Manejar estados globales y lógica de página
- Usar hooks personalizados para lógica compleja

## Ejemplo de Página

```typescript
import { useEffect } from 'react';
import { useIncidencias } from '@/hooks/useIncidencias';
import { IncidenciaList } from '@/components/features/incidencias/IncidenciaList';
import { MainLayout } from '@/components/layout/MainLayout';

export const IncidenciasPage: React.FC = () => {
  const { incidencias, loading, error, fetchIncidencias } = useIncidencias();

  useEffect(() => {
    fetchIncidencias();
  }, []);

  if (loading) return <div>Cargando...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <MainLayout>
      <h1>Gestión de Incidencias</h1>
      <IncidenciaList incidencias={incidencias} />
    </MainLayout>
  );
};

export default IncidenciasPage;
```
