# Context - Contextos de React

Estado global y contextos de la aplicación usando Context API.

## Estructura

```
context/
├── AuthContext.tsx        # Contexto de autenticación
├── ThemeContext.tsx       # Contexto de tema (dark/light)
├── NotificationContext.tsx # Contexto de notificaciones
└── index.ts              # Barrel export
```

## Convenciones

- Sufijo `Context.tsx`
- Incluir Provider y hook personalizado
- Exportar tipo del contexto
- Manejar loading y error states

## Ejemplo

```typescript
// AuthContext.tsx
import { createContext, useContext, useState, type ReactNode } from 'react';
import type { Usuario } from '@/types/usuario.types';

interface AuthContextType {
  usuario: Usuario | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [loading, setLoading] = useState(false);

  const login = async (email: string, password: string) => {
    setLoading(true);
    try {
      // Lógica de login
      const data = await authService.login({ email, password });
      setUsuario(data.usuario);
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setUsuario(null);
    localStorage.removeItem('token');
  };

  const value = {
    usuario,
    isAuthenticated: !!usuario,
    login,
    logout,
    loading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider');
  }
  return context;
};
```
