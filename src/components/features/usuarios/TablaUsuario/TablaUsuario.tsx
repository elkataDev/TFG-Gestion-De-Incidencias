import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paginacion from '../../../common/Paginacion/Paginacion';
import Paper from '@mui/material/Paper';
import { useState } from 'react';
import './TablaUsuario.css';
import { EstadoBadge } from '@/components/common/EstadoBadge/EstadoBadge';

//Se debe modificar cuando se implemente la base de datos
export type UsuarioData = {
  id: string;
  nombre: string;
  correo: string;
  rol: 'admin' | 'user' | 'tecnico';
  estado: 'activo' | 'inactivo';
};
interface TablaUsuarioProps {
  usuarios: UsuarioData[];
}

export default function TablaUsuario({ usuarios }: TablaUsuarioProps) {
  const [paginaActual, setPaginaActual] = useState(0);
  const filasPorPagina = 5;

  const filasVisibles = usuarios.slice(
    paginaActual * filasPorPagina,
    (paginaActual + 1) * filasPorPagina
  );

  return (
    <>
      <TableContainer component={Paper} className="table-container">
        <Table sx={{ minWidth: 650 }}>
          <TableHead className="table-header">
            <TableRow>
              <TableCell align="center">Nombre</TableCell>
              <TableCell align="center">Correo Electronico</TableCell>
              <TableCell align="center">Rol</TableCell>
              <TableCell align="center">Estado</TableCell>
            </TableRow>
          </TableHead>
          <TableBody className="table-body">
            {filasVisibles.map((row) => (
              <TableRow className="table-row" key={row.id}>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  {row.nombre}
                </TableCell>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  {row.correo}
                </TableCell>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  {row.rol}
                </TableCell>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  <EstadoBadge estado={row.estado} />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Paginacion
        total={usuarios.length}
        filasPorPagina={filasPorPagina}
        paginaActual={paginaActual}
        onPageChange={setPaginaActual}
      ></Paginacion>
    </>
  );
}
