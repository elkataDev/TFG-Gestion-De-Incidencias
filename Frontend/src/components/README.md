# Components - Componentes Reutilizables

Esta carpeta contiene todos los componentes React del proyecto.

## Estructura

### 📁 `common/`

Componentes genéricos y reutilizables en toda la aplicación.

- Botones, Inputs, Modales, Cards, etc.
- Componentes sin lógica de negocio específica
- Ejemplos: `Button.tsx`, `Input.tsx`, `Modal.tsx`, `Card.tsx`

### 📁 `layout/`

Componentes de estructura y diseño de la aplicación.

- Header, Footer, Sidebar, Navigation
- Layouts generales de páginas
- Ejemplos: `Header.tsx`, `Sidebar.tsx`, `MainLayout.tsx`

### 📁 `features/`

Componentes específicos de cada funcionalidad/módulo.

#### `features/incidencias/`

Componentes relacionados con la gestión de incidencias.

- `IncidenciaCard.tsx` - Tarjeta de incidencia
- `IncidenciaForm.tsx` - Formulario crear/editar
- `IncidenciaList.tsx` - Lista de incidencias
- `IncidenciaDetail.tsx` - Detalle de incidencia
- `IncidenciaFilters.tsx` - Filtros de búsqueda

#### `features/usuarios/`

Componentes relacionados con usuarios.

- `UsuarioCard.tsx` - Tarjeta de usuario
- `UsuarioForm.tsx` - Formulario de usuario
- `UsuarioList.tsx` - Lista de usuarios

#### `features/auth/`

Componentes relacionados con autenticación.

- `LoginForm.tsx` - Formulario de login
- `RegisterForm.tsx` - Formulario de registro
- `ForgotPassword.tsx` - Recuperar contraseña

## Convenciones

- Un componente por archivo
- Nombres en PascalCase
- Usar TypeScript con tipos explícitos
- Exportar componente como default y tipos como named exports
- Incluir props interface en el mismo archivo

## Ejemplo de Componente

```typescript
interface ButtonProps {
  label: string;
  onClick: () => void;
  variant?: 'primary' | 'secondary';
  disabled?: boolean;
}

export const Button: React.FC<ButtonProps> = ({
  label,
  onClick,
  variant = 'primary',
  disabled = false
}) => {
  return (
    <button
      className={`btn btn-${variant}`}
      onClick={onClick}
      disabled={disabled}
    >
      {label}
    </button>
  );
};

export default Button;
```
