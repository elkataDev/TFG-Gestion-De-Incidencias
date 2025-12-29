import React, { useEffect, useState } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Paginacion from '../Paginacion/Paginacion';
import './Tabla.css';

interface TablaGenericaProps<T> {
  endpoint?: string;
  data?: T[];
  filasPorPagina?: number;
  renderCustomCell?: (key: string, value: unknown, row: T) => React.ReactNode; // Para celdas personalizadas (como EstadoBadge)
}

export default function TablaGenerica<T extends Record<string, unknown>>({
  endpoint,
  data,
  filasPorPagina = 5,
  renderCustomCell,
}: TablaGenericaProps<T>) {
  const [rows, setRows] = useState<T[]>([]);
  const [paginaActual, setPaginaActual] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!endpoint) return;

    setLoading(true);

    fetch(endpoint)
      .then((res) => res.json())
      .then((json: T[]) => setRows(json))
      .catch((error) => console.error('Error al cargar datos:', error))
      .finally(() => setLoading(false));
  }, [endpoint]);

  // Si se pasan datos por props, úsalos directamente
  useEffect(() => {
    if (data) setRows(data);
  }, [data]);

  const columnas: string[] = rows.length > 0 ? Object.keys(rows[0]!) : [];

  const filasVisibles = rows.slice(
    paginaActual * filasPorPagina,
    (paginaActual + 1) * filasPorPagina
  );

  return (
    <TableContainer
      component={Paper}
      className="table-container"
      sx={{ backgroundColor: 'var(--color-bg-card)' }}
    >
      <Table sx={{ minWidth: 650 }}>
        <TableHead className="table-header">
          <TableRow>
            {columnas.map((col) => (
              <TableCell key={col} align="center">
                {col.toUpperCase()}
              </TableCell>
            ))}
          </TableRow>
        </TableHead>

        <TableBody className="table-body">
          {loading ? (
            <TableRow>
              <TableCell colSpan={columnas.length} align="center">
                Cargando datos...
              </TableCell>
            </TableRow>
          ) : filasVisibles.length > 0 ? (
            filasVisibles.map((row, index) => (
              <TableRow className="table-row" key={index}>
                {columnas.map((key) => (
                  <TableCell key={key} align="center">
                    {renderCustomCell
                      ? renderCustomCell(key, row[key], row)
                      : (row[key] as React.ReactNode)}
                  </TableCell>
                ))}
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={columnas.length} align="center">
                No hay datos disponibles
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>

      <Paginacion
        total={rows.length}
        filasPorPagina={filasPorPagina}
        paginaActual={paginaActual}
        onPageChange={setPaginaActual}
      />
    </TableContainer>
  );
}
