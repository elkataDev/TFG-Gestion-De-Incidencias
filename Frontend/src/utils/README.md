# Utils - Funciones Utilitarias

Funciones helper y utilidades reutilizables.

## Estructura

```
utils/
├── date.utils.ts          # Formateo y manejo de fechas
├── string.utils.ts        # Manipulación de strings
├── validation.utils.ts    # Validaciones
├── format.utils.ts        # Formateo de datos
└── index.ts              # Barrel export
```

## Convenciones

- Funciones puras sin side effects
- Sufijo `.utils.ts`
- Exportar funciones individuales
- Documentar con JSDoc
- Incluir tests unitarios

## Ejemplo

```typescript
// date.utils.ts

/**
 * Formatea una fecha al formato español
 * @param date - Fecha a formatear
 * @returns String con formato dd/MM/yyyy
 */
export const formatDate = (date: Date | string): string => {
  const d = typeof date === 'string' ? new Date(date) : date;
  return d.toLocaleDateString('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

/**
 * Calcula la diferencia en días entre dos fechas
 */
export const daysDifference = (date1: Date, date2: Date): number => {
  const diff = Math.abs(date1.getTime() - date2.getTime());
  return Math.ceil(diff / (1000 * 60 * 60 * 24));
};

/**
 * Verifica si una fecha es hoy
 */
export const isToday = (date: Date): boolean => {
  const today = new Date();
  return date.toDateString() === today.toDateString();
};
```
