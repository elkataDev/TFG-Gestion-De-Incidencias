package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import lombok.*;
import org.hibernate.Hibernate;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para transferir información de artículos del inventario.
 * <p>
 * Representa equipamiento informático del centro educativo, incluyendo
 * computadoras, impresoras, proyectores, etc.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see InventarioEntity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {
    /**
     * Identificador único del artículo.
     */
    private Long id;

    /**
     * Nombre del artículo (ej: "PC-101", "Impresora HP LaserJet").
     */
    private String nombre;

    /**
     * Descripción detallada del artículo.
     */
    private String descripcion;

    /**
     * Código QR único para identificación rápida.
     */
    private String codigoQR;

    /**
     * Estado actual del artículo (DISPONIBLE, EN_USO, DAÑADO).
     */
    private String estado;

    /**
     * Categoría del artículo (COMPUTADORA, IMPRESORA, etc.).
     */
    private String categoria;

    /**
     * Fecha de ingreso del artículo al inventario.
     */
    private LocalDateTime fechaIngreso;

    /**
     * Identificador del aula donde se encuentra el artículo.
     */
    private Long aulaId;

    /**
     * Nombre del aula donde se encuentra el artículo.
     */
    private String nombreAula;

    /**
     * Convierte una entidad InventarioEntity a su correspondiente DTO.
     *
     * @param entity entidad de inventario a convertir
     * @return InventarioDTO con los datos de la entidad, o null si la entidad es null
     */
    public static InventarioDTO fromEntity(InventarioEntity entity) {
        if (entity == null) return null;

        Long aulaId = null;
        String aulaName = "Sin asignar";
        if (entity.getAula() != null && Hibernate.isInitialized(entity.getAula())) {
            aulaId = entity.getAula().getId();
            aulaName = entity.getAula().getNombre();
        }

        return InventarioDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .codigoQR(entity.getCodigoQR())
                .estado(entity.getEstado() != null ? entity.getEstado().name() : null)
                .categoria(entity.getCategoria() != null ? entity.getCategoria().name() : null)
                .fechaIngreso(entity.getFechaIngreso())
                .aulaId(aulaId)
                .nombreAula(aulaName)
                .build();
    }
}
