import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import BasicTable from '@/components/common/Tabla/Tabla';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import { useNavigate } from 'react-router-dom';

const activosEjemplo = [
  {
    id: '#1',
    nombre: 'Laptop Dell Latitude 5510',
    ubicacion: 'Oficina 1',
    categoria: 'Computadora',
    acciones: null,
  },
  {
    id: '#2',
    nombre: 'Impresora HP LaserJet M404',
    ubicacion: 'Biblioteca',
    categoria: 'Impresora',
  },
  {
    id: '#3',
    nombre: 'Proyector Epson X05',
    ubicacion: 'Aula 3',
    categoria: 'Proyector',
  },
  {
    id: '#4',
    nombre: 'Router Cisco 2901',
    ubicacion: 'Sala de servidores',
    categoria: 'Red',
  },
  {
    id: '#5',
    nombre: 'Monitor Samsung 24”',
    ubicacion: 'Laboratorio 1',
    categoria: 'Monitor',
  },
  {
    id: '#6',
    nombre: 'Teclado Logitech K380',
    ubicacion: 'Oficina 2',
    categoria: 'Periférico',
  },
  {
    id: '#7',
    nombre: 'Switch TP-Link TL-SG108',
    ubicacion: 'Laboratorio 2',
    categoria: 'Red',
  },
  {
    id: '#8',
    nombre: 'PC HP ProDesk 400',
    ubicacion: 'Oficina 3',
    categoria: 'Computadora',
  },
  {
    id: '#9',
    nombre: 'Proyector BenQ MW560',
    ubicacion: 'Aula 1',
    categoria: 'Proyector',
  },
  {
    id: '#10',
    nombre: 'Impresora Epson L3150',
    ubicacion: 'Oficina 4',
    categoria: 'Impresora',
  },
  {
    id: '#11',
    nombre: 'Monitor LG UltraWide 29”',
    ubicacion: 'Laboratorio 3',
    categoria: 'Monitor',
  },
  {
    id: '#12',
    nombre: 'Laptop Lenovo ThinkPad E14',
    ubicacion: 'Oficina 5',
    categoria: 'Computadora',
  },
  {
    id: '#13',
    nombre: 'Switch D-Link DGS-1016A',
    ubicacion: 'Sala de redes',
    categoria: 'Red',
  },
  {
    id: '#14',
    nombre: 'Cámara de seguridad Hikvision',
    ubicacion: 'Entrada principal',
    categoria: 'Seguridad',
  },
  {
    id: '#15',
    nombre: 'Servidor Dell PowerEdge R740',
    ubicacion: 'Sala de servidores',
    categoria: 'Servidor',
  },
  {
    id: '#16',
    nombre: 'Proyector ViewSonic PA503S',
    ubicacion: 'Aula 5',
    categoria: 'Proyector',
  },
  {
    id: '#17',
    nombre: 'Laptop Acer Aspire 5',
    ubicacion: 'Oficina 6',
    categoria: 'Computadora',
  },
  {
    id: '#18',
    nombre: 'Monitor Philips 22”',
    ubicacion: 'Biblioteca',
    categoria: 'Monitor',
  },
  {
    id: '#19',
    nombre: 'PC Lenovo ThinkCentre M720',
    ubicacion: 'Laboratorio 4',
    categoria: 'Computadora',
  },
  {
    id: '#20',
    nombre: 'Teclado Redragon K552',
    ubicacion: 'Laboratorio 5',
    categoria: 'Periférico',
  },
  {
    id: '#21',
    nombre: 'Cámara Logitech C920',
    ubicacion: 'Oficina 7',
    categoria: 'Periférico',
  },
  {
    id: '#22',
    nombre: 'Impresora Brother HL-L2350DW',
    ubicacion: 'Oficina 8',
    categoria: 'Impresora',
  },
  {
    id: '#23',
    nombre: 'Servidor HP ProLiant DL380',
    ubicacion: 'Sala de servidores',
    categoria: 'Servidor',
  },
  {
    id: '#24',
    nombre: 'Router Mikrotik hEX',
    ubicacion: 'Sala de redes',
    categoria: 'Red',
  },
  {
    id: '#25',
    nombre: 'Proyector Sony VPL-DX221',
    ubicacion: 'Aula 2',
    categoria: 'Proyector',
  },
  {
    id: '#26',
    nombre: 'Monitor Dell P2422H',
    ubicacion: 'Oficina 9',
    categoria: 'Monitor',
  },
  {
    id: '#27',
    nombre: 'Laptop HP EliteBook 840',
    ubicacion: 'Oficina 10',
    categoria: 'Computadora',
  },
  {
    id: '#28',
    nombre: 'PC ASUS ExpertCenter D500',
    ubicacion: 'Laboratorio 6',
    categoria: 'Computadora',
  },
  {
    id: '#29',
    nombre: 'Switch Cisco Catalyst 2960',
    ubicacion: 'Sala de servidores',
    categoria: 'Red',
  },
  {
    id: '#30',
    nombre: 'Cámara Dahua IP',
    ubicacion: 'Pasillo principal',
    categoria: 'Seguridad',
  },
];

function Selects() {
  return (
    <span className="selects-container">
      <SelectAutoWidth
        inputText="Estado"
        options={[{ label: 'Opcion1' }, { label: 'Opcion2' }]}
      ></SelectAutoWidth>
      <SelectAutoWidth
        inputText="Categoria"
        options={[{ label: 'Opcion1' }, { label: 'Opcion2' }]}
      ></SelectAutoWidth>
      <SelectAutoWidth
        inputText="Ubicacion"
        options={[{ label: 'Opcion1' }, { label: 'Opcion2' }]}
      ></SelectAutoWidth>
    </span>
  );
}

export default function PagActivos() {
  const navigate = useNavigate();

  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averias</h1>
        <BotonPrimario
          startIcon=""
          text="+ Nuevo Activo"
          onClick={() => {
            void navigate('/nuevoActivo');
          }}
        ></BotonPrimario>
      </div>
      <Selects />
      <div className="table-container">
        <BasicTable
          endpoint="http://localhost:5555/api/inventario"
          filasPorPagina={5}
          extraColumns={['acciones']}
          renderCustomCell={(key, value, row) => {
            if (key === 'acciones') {
              return (
                <button
                  className="link-editar"
                  onClick={() => void navigate(`/editarActivo/${row.id}`)}
                >
                  Editar
                </button>
              );
            }

            if (key === 'estado') {
              return <EstadoBadge estado={String(value)} />;
            }

            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
