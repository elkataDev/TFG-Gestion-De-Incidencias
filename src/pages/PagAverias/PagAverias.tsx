import BasicTable from '@/components/common/Tabla/Tabla';
import BotonPrimario from '@/components/common/BotonPrimario/BotonPrimario';
import SelectAutoWidth from '@/components/common/Select/Select';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';
import './PagAverias.css';

const datosEjemplo = [
  {
    id: '#1',
    asunto: 'Problemas de conexión en la biblioteca',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Biblioteca',
    reporte: 'El Sergio',
    asignacion: 'El Toni',
    fecha: '2025-10-14',
  },
  {
    id: '#2',
    asunto: 'Error de software',
    estado: 'Resuelto',
    categoria: 'Software',
    ubicacion: 'Laboratorio 2',
    reporte: 'Juan Pérez',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-15',
  },
  {
    id: '#3',
    asunto: 'Impresora no funciona',
    estado: 'Abierto',
    categoria: 'Hardware',
    ubicacion: 'Oficina 1',
    reporte: 'María López',
    asignacion: 'Carlos Ruiz',
    fecha: '2025-10-16',
  },
  {
    id: '#4',
    asunto: 'Fallo en el proyector',
    estado: 'En progreso',
    categoria: 'Hardware',
    ubicacion: 'Aula 3',
    reporte: 'Ana Gómez',
    asignacion: 'Luis Fernández',
    fecha: '2025-10-16',
  },
  {
    id: '#5',
    asunto: 'Problema con la contraseña',
    estado: 'Resuelto',
    categoria: 'Software',
    ubicacion: 'Laboratorio 1',
    reporte: 'Pedro Martínez',
    asignacion: 'El Toni',
    fecha: '2025-10-17',
  },
  {
    id: '#6',
    asunto: 'Caída del servidor',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Sala de servidores',
    reporte: 'Sofía Ramírez',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-17',
  },
  {
    id: '#7',
    asunto: 'Teclado dañado',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Oficina 2',
    reporte: 'Miguel Sánchez',
    asignacion: 'Carlos Ruiz',
    fecha: '2025-10-18',
  },
  {
    id: '#8',
    asunto: 'Problema de software educativo',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Aula 5',
    reporte: 'Laura Díaz',
    asignacion: 'Luis Fernández',
    fecha: '2025-10-18',
  },
  {
    id: '#9',
    asunto: 'Red lenta',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Laboratorio 3',
    reporte: 'Jorge Torres',
    asignacion: 'El Toni',
    fecha: '2025-10-19',
  },
  {
    id: '#10',
    asunto: 'Pantalla rota',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Oficina 3',
    reporte: 'Carla Morales',
    asignacion: 'Carlos Ruiz',
    fecha: '2025-10-19',
  },
  {
    id: '#11',
    asunto: 'Error en la aplicación contable',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Oficina 4',
    reporte: 'David Pérez',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-20',
  },
  {
    id: '#12',
    asunto: 'Fallo en la conexión WiFi',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Biblioteca',
    reporte: 'Elena Jiménez',
    asignacion: 'El Toni',
    fecha: '2025-10-20',
  },
  {
    id: '#13',
    asunto: 'Problema de audio en aulas',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Aula 2',
    reporte: 'Fernando Castillo',
    asignacion: 'Luis Fernández',
    fecha: '2025-10-21',
  },
  {
    id: '#14',
    asunto: 'Instalación de nuevo software',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Laboratorio 4',
    reporte: 'Isabel Navarro',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-21',
  },
  {
    id: '#15',
    asunto: 'Servidor de correo caído',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Sala de servidores',
    reporte: 'Antonio Ruiz',
    asignacion: 'El Toni',
    fecha: '2025-10-22',
  },
  {
    id: '#16',
    asunto: 'Problema con cámara de seguridad',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Entrada principal',
    reporte: 'Paula Torres',
    asignacion: 'Carlos Ruiz',
    fecha: '2025-10-22',
  },
  {
    id: '#17',
    asunto: 'Error en sistema de notas',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Oficina de administración',
    reporte: 'Ricardo Medina',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-23',
  },
  {
    id: '#18',
    asunto: 'Falla en la red de laboratorio',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Laboratorio 5',
    reporte: 'Verónica Salas',
    asignacion: 'El Toni',
    fecha: '2025-10-23',
  },
  {
    id: '#19',
    asunto: 'Teclado del proyector dañado',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Aula 1',
    reporte: 'Eduardo Flores',
    asignacion: 'Luis Fernández',
    fecha: '2025-10-24',
  },
  {
    id: '#20',
    asunto: 'Error al guardar archivos',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Laboratorio 6',
    reporte: 'Marina López',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-24',
  },
  {
    id: '#21',
    asunto: 'Problema de conectividad remota',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Oficina 5',
    reporte: 'Santiago Ortega',
    asignacion: 'El Toni',
    fecha: '2025-10-25',
  },
  {
    id: '#22',
    asunto: 'Monitor no enciende',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Oficina 6',
    reporte: 'Valeria Mendoza',
    asignacion: 'Carlos Ruiz',
    fecha: '2025-10-25',
  },
  {
    id: '#23',
    asunto: 'Actualización de sistema operativo',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Laboratorio 7',
    reporte: 'Alberto Gómez',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-26',
  },
  {
    id: '#24',
    asunto: 'Falla en la red principal',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Sala de servidores',
    reporte: 'Claudia Ramírez',
    asignacion: 'El Toni',
    fecha: '2025-10-26',
  },
  {
    id: '#25',
    asunto: 'Problema con lector de código',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Oficina 7',
    reporte: 'Diego Herrera',
    asignacion: 'Luis Fernández',
    fecha: '2025-10-27',
  },
  {
    id: '#26',
    asunto: 'Error en software de contabilidad',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Oficina 8',
    reporte: 'Patricia Vega',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-27',
  },
  {
    id: '#27',
    asunto: 'WiFi intermitente',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Aula 4',
    reporte: 'Héctor Salazar',
    asignacion: 'El Toni',
    fecha: '2025-10-28',
  },
  {
    id: '#28',
    asunto: 'Impresora no imprime',
    estado: 'Resuelto',
    categoria: 'Hardware',
    ubicacion: 'Laboratorio 8',
    reporte: 'Sonia Castillo',
    asignacion: 'Carlos Ruiz',
    fecha: '2025-10-28',
  },
  {
    id: '#29',
    asunto: 'Actualización de seguridad',
    estado: 'En progreso',
    categoria: 'Software',
    ubicacion: 'Oficina 9',
    reporte: 'Javier Molina',
    asignacion: 'Lucía Torres',
    fecha: '2025-10-29',
  },
  {
    id: '#30',
    asunto: 'Fallo en router principal',
    estado: 'Abierto',
    categoria: 'Red',
    ubicacion: 'Sala de servidores',
    reporte: 'Natalia Campos',
    asignacion: 'El Toni',
    fecha: '2025-10-29',
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

export default function PagAverias() {
  return (
    <div className="pag-averias-container">
      <div className="header-container">
        <h1>Listado de Tickets de Averias</h1>
        <BotonPrimario startIcon="" text="+ Nuevo Ticket"></BotonPrimario>
      </div>
      <Selects />
      <div className="table-container">
        <BasicTable
          data={datosEjemplo}
          filasPorPagina={5}
          renderCustomCell={(key, value, _row) => {
            if (key === 'estado') return <EstadoBadge estado={String(value)} />;
            return value as React.ReactNode;
          }}
        />
      </div>
    </div>
  );
}
