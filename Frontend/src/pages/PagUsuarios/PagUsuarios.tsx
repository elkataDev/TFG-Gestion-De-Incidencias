import { useState } from 'react';
import './PagUsarios.css';
import BasicTable from '@/components/common/Tabla/Tabla';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { apiJson } from '@/services/api/apiService';

export default function PagUsuarios() {
  const role = localStorage.getItem('role');
  const [refreshKey, setRefreshKey] = useState(0);

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
            if (key === 'rol') return <EstadoBadge estado={String(value)} />;
            if (key === 'activo') return <EstadoBadge estado={value ? 'ACTIVO' : 'INACTIVO'} />;
            if (key === 'acciones' && role === 'ADMIN') {
              return (
                <button
                  onClick={() => void handleDelete(Number(row.id))}
                  style={{
                    backgroundColor: '#e53935', color: 'white', border: 'none', padding: '5px 12px',
                    borderRadius: '4px', cursor: 'pointer', fontWeight: 600
                  }}
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
