import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paginacion from '../Paginacion/Paginacion';
import Paper from '@mui/material/Paper';
import { EstadoBadge } from '../EstadoBadge/EstadoBadge';
import { useState } from 'react';
import './Tabla.css';

//Se debe modificar cuando se implemente la base de datos
function createData(
  id: string,
  asunto: string,
  estado: string,
  categoria: string,
  ubicacion: string,
  reporte: string,
  asignacion: string,
  fecha: string
) {
  return { id, asunto, estado, categoria, ubicacion, reporte, asignacion, fecha };
}

export default function BasicTable() {
  //Por modificar para hacer un fetch
  const [paginaActual, setPaginaActual] = useState(0);
  const filasPorPagina = 5;

  const rows: ReturnType<typeof createData>[] = [];

  const estados = ['Abierto', 'En progreso', 'Resuelto', 'Cerrado'];
  const categorias = ['Red', 'Software', 'Hardware'];

  for (let i = 0; i <= 10; i++) {
    const estado: string = estados[i % estados.length]!;
    const categoria: string = categorias[i % categorias.length]!;

    rows.push(
      createData(
        `#${i + 1}`,
        `Problema de ejemplo ${i + 1}`,
        estado,
        categoria,
        'Biblioteca',
        'El Sergio',
        'El Toni',
        '2025-10-14'
      )
    );
  }

  const filasVisibles = rows.slice(
    paginaActual * filasPorPagina,
    (paginaActual + 1) * filasPorPagina
  );

  return (
    <>
      <TableContainer component={Paper} className="table-container">
        <Table sx={{ minWidth: 650 }}>
          <TableHead className="table-header">
            <TableRow>
              <TableCell>Ticket ID</TableCell>
              <TableCell align="left">Asunto</TableCell>
              <TableCell align="right">Estado</TableCell>
              <TableCell align="right">Categoría</TableCell>
              <TableCell align="right">Ubicación</TableCell>
              <TableCell align="right">Reportado Por</TableCell>
              <TableCell align="right">Asignado A</TableCell>
              <TableCell align="right">Fecha Creación</TableCell>
            </TableRow>
          </TableHead>
          <TableBody className="table-body">
            {filasVisibles.map((row) => (
              <TableRow className="table-row" key={row.id}>
                <TableCell className="table-row" component="th" scope="row">
                  {row.id}
                </TableCell>
                <TableCell sx={{ maxWidth: 250 }} className="table-row" component="th" scope="row">
                  {row.asunto}
                </TableCell>
                <TableCell sx={{ maxWidth: 50 }} className="table-row" align="right">
                  <EstadoBadge estado={row.estado} />
                </TableCell>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  {row.categoria}
                </TableCell>
                <TableCell className="table-row" align="right" component="th" scope="row">
                  {row.ubicacion}
                </TableCell>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  {row.reporte}
                </TableCell>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  {row.asignacion}
                </TableCell>
                <TableCell className="table-row" align="center" component="th" scope="row">
                  {row.fecha}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Paginacion
        total={rows.length}
        filasPorPagina={filasPorPagina}
        paginaActual={paginaActual}
        onPageChange={setPaginaActual}
      ></Paginacion>
    </>
  );
}
