package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.MovimientoInventarioEntity;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Movimientos de Inventario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioDTO {

    private Long id;
    private Long inventarioId;
    private String nombreActivo;
    private Long aulaOrigenId;
    private String nombreAulaOrigen;
    private Long aulaDestinoId;
    private String nombreAulaDestino;
    private Long responsableId;
    private String nombreResponsable;
    private String motivo;
    private LocalDateTime fechaMovimiento;

    public static MovimientoInventarioDTO fromEntity(MovimientoInventarioEntity entity) {
        if (entity == null) return null;

        Long inventarioId = null;
        String nombreActivo = "Desconocido";
        if (entity.getInventario() != null && Hibernate.isInitialized(entity.getInventario())) {
            inventarioId = entity.getInventario().getId();
            nombreActivo = entity.getInventario().getNombre();
        }

        Long aulaOrigenId = null;
        String nombreAulaOrigen = "Almacén / Sin ubicación";
        if (entity.getAulaOrigen() != null && Hibernate.isInitialized(entity.getAulaOrigen())) {
            aulaOrigenId = entity.getAulaOrigen().getId();
            nombreAulaOrigen = entity.getAulaOrigen().getNombre();
        }

        Long aulaDestinoId = null;
        String nombreAulaDestino = "Almacén / Sin ubicación";
        if (entity.getAulaDestino() != null && Hibernate.isInitialized(entity.getAulaDestino())) {
            aulaDestinoId = entity.getAulaDestino().getId();
            nombreAulaDestino = entity.getAulaDestino().getNombre();
        }

        Long responsableId = null;
        String nombreResponsable = "Desconocido";
        if (entity.getResponsable() != null && Hibernate.isInitialized(entity.getResponsable())) {
            responsableId = entity.getResponsable().getId();
            nombreResponsable = entity.getResponsable().getNombreUsuario();
        }

        return MovimientoInventarioDTO.builder()
                .id(entity.getId())
                .inventarioId(inventarioId)
                .nombreActivo(nombreActivo)
                .aulaOrigenId(aulaOrigenId)
                .nombreAulaOrigen(nombreAulaOrigen)
                .aulaDestinoId(aulaDestinoId)
                .nombreAulaDestino(nombreAulaDestino)
                .responsableId(responsableId)
                .nombreResponsable(nombreResponsable)
                .motivo(entity.getMotivo())
                .fechaMovimiento(entity.getFechaMovimiento())
                .build();
    }
}
