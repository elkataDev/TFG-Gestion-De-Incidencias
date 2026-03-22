import React, { useEffect, useState } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Paginacion from '../Paginacion/Paginacion';
import { apiFetch } from '@/services/api/apiService';
import './Tabla.css';

interface TablaGenericaProps<T extends Record<string, unknown>> {
  endpoint?: string;
  data?: T[];
  extraColumns?: string[];
  filasPorPagina?: number;
  renderCustomCell?: (key: string, value: unknown, row: Record<string, unknown>) => React.ReactNode;
}

export default function TablaGenerica<T extends Record<string, unknown>>({
  endpoint,
  data,
  filasPorPagina = 5,
  renderCustomCell,
  extraColumns = [],
}: TablaGenericaProps<T>) {
  const [rows, setRows] = useState<Record<string, unknown>[]>([]);
  const [paginaActual, setPaginaActual] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Función para aplanar objetos anidados
  const flattenObject = (obj: Record<string, unknown>): Record<string, unknown> => {
    const flattened: Record<string, unknown> = {};
    Object.entries(obj).forEach(([key, value]) => {
      if (value && typeof value === 'object' && !Array.isArray(value)) {
        Object.entries(value as Record<string, unknown>).forEach(([subKey, subValue]) => {
          flattened[subKey] = subValue;
        });
      } else {
        flattened[key] = value;
      }
    });
    return flattened;
  };

  // Cargar datos desde endpoint
  useEffect(() => {
    if (!endpoint) return;

    setLoading(true);
    setError(null);

    apiFetch(endpoint)
      .then(async (res) => {
        const text = await res.text();
        if (!text) return [];

        try {
          const jsonData = JSON.parse(text);
          return Array.isArray(jsonData) ? jsonData : [jsonData];
        } catch (err) {
          console.error('Error parseando JSON:', text, err);
          return [];
        }
      })
      .then((data) => {
        setRows(data.map(flattenObject));
      })
      .catch((err: unknown) => {
        console.error('Error al cargar datos:', err);
        if (err instanceof Error) setError(err.message);
      })
      .finally(() => setLoading(false));
  }, [endpoint]);

  // Si se pasan datos por props
  useEffect(() => {
    if (data) setRows(data.map(flattenObject));
  }, [data]);

  const columnas: string[] = rows.length > 0 ? [...Object.keys(rows[0]!), ...extraColumns] : [];
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
          ) : error ? (
            <TableRow>
              <TableCell colSpan={columnas.length} align="center">
                {error}
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
