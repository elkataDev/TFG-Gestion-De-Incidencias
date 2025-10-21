export function EstadoBadge({ estado }: { estado: string }) {
  const clase = `estado-tag estado-${estado.toLowerCase().replace(' ', '-')}`;
  return <span className={clase}>{estado}</span>;
}
