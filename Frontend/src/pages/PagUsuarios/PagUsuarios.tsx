import { useState } from 'react';
import './PagUsarios.css';
import BasicTable from '@/components/common/Tabla/Tabla';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { apiJson } from '@/services/api/apiService';

const ROLES = ['USUARIO', 'TECNICO', 'ADMIN'];

export default function PagUsuarios() {
  const role = localStorage.getItem('role');
  const username = localStorage.getItem('username');
  const [refreshKey, setRefreshKey] = useState(0);
  const [savingRoleId, setSavingRoleId] = useState<number | null>(null);

  const handleDelete = async (id: number) => {
    if (!window.confirm('¿Está seguro de eliminar este usuario?')) return;
    try {
      await apiJson(`/usuarios/${id}`, { method: 'DELETE' });
      setRefreshKey((prev) => prev + 1);
    } catch (error) {
      console.error('Error eliminando usuario:', error);
      alert('Error al eliminar el usuario. Compruebe que tiene permisos de administrador.');
    }
  };

  const handleRoleChange = async (id: number, nuevoRol: string, row: Record<string, unknown>) => {
    const nombreUsuario = String(row.nombreUsuario ?? '');
    const currentRol = String(row.rol ?? '');
    if (!nuevoRol || nuevoRol === currentRol) return;

    const isSelf = username && nombreUsuario === username;
    const warning = isSelf
      ? `Vas a cambiar TU propio rol de ${currentRol} a ${nuevoRol}. Si te quitas ADMIN puedes perder acceso a esta pantalla. ¿Continuar?`
      : `¿Cambiar rol de ${nombreUsuario} de ${currentRol} a ${nuevoRol}?`;

    if (!window.confirm(warning)) return;

    try {
      setSavingRoleId(id);
      await apiJson(`/usuarios/${id}/rol`, {
        method: 'PATCH',
        body: JSON.stringify({ rol: nuevoRol }),
      });
      if (isSelf) {
        localStorage.setItem('role', nuevoRol);
      }
      setRefreshKey((prev) => prev + 1);
    } catch (error) {
      console.error('Error cambiando rol:', error);
      alert(error instanceof Error ? error.message : 'Error al cambiar el rol');
    } finally {
      setSavingRoleId(null);
    }
  };

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Gestión de Usuarios</h1>
      </div>
      <div className="table-container">
        <BasicTable
          key={refreshKey}
          endpoint="/usuarios"
          filasPorPagina={5}
          extraColumns={role === 'ADMIN' ? ['acciones'] : []}
          renderCustomCell={(key, value, row) => {
            if (key === 'rol') {
              if (role !== 'ADMIN') return <EstadoBadge estado={String(value)} />;
              const userId = Number(row.id);
              return (
                <select
                  className="role-select"
                  value={String(value ?? 'USUARIO')}
                  disabled={savingRoleId === userId}
                  onChange={(e) => void handleRoleChange(userId, e.target.value, row)}
                >
                  {ROLES.map((rol) => (
                    <option key={rol} value={rol}>
                      {rol}
                    </option>
                  ))}
                </select>
              );
            }
            if (key === 'activo') return <EstadoBadge estado={value ? 'ACTIVO' : 'INACTIVO'} />;
            if (key === 'acciones' && role === 'ADMIN') {
              return (
                <button
                  className="app-danger-button"
                  onClick={() => void handleDelete(Number(row.id))}
                >
                  ELIMINAR
                </button>
              );
            }
            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
