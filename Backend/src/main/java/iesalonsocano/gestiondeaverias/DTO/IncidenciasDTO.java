package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para transferir información de incidencias técnicas.
 * <p>
 * Incluye datos de la incidencia y referencias al aula y usuario asociados,
 * evitando la carga de relaciones completas y optimizando el tráfico de red.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see IncidenciasEntity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidenciasDTO {
    /**
     * Identificador único de la incidencia.
     */
    private Long id;

    /**
     * Título breve de la incidencia.
     */
    private String titulo;

    /**
     * Descripción detallada del problema reportado.
     */
    private String descripcion;

    /**
     * Estado actual de la incidencia (ABIERTO, EN_PROGRESO, RESUELTO, etc.).
     */
    private String estado;

    /**
     * Fecha y hora en que se reportó la incidencia.
     */
    private String categoria; // <-- Enum como String
    private String nombreAula; // <-- nombre del aula
    
    /**
     * Nombre de usuario del que reportó la incidencia (para mostrar en UI).
     */
    private String nombreUsuario;
    
    private LocalDateTime fechaReporte;

    /**
     * Fecha y hora en que se cerró la incidencia (null si aún está abierta).
     */
    private LocalDateTime fechaCierre;

    /**
     * Identificador del aula donde ocurrió la incidencia.
     */
    private Long aulaId;

    /**
     * Identificador del usuario que reportó la incidencia.
     */
    private Long usuarioId;

    /**
     * URL del archivo adjunto asociado.
     */
    private String adjuntoUrl;

    /**
     * Convierte una entidad IncidenciasEntity a su correspondiente DTO.
     * <p>
     * Realiza un mapeo seguro de las relaciones, manejando referencias null.
     * </p>
     *
     * @param entity entidad de incidencia a convertir
     * @return IncidenciasDTO con los datos de la entidad, o null si la entidad es null
     */
    // --- CONVERSOR ---
    public static IncidenciasDTO fromEntity(IncidenciasEntity entity) {
        if (entity == null) {
            return null;
        }

        return IncidenciasDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado() != null ? entity.getEstado().name() : null)
                .categoria(entity.getCategoria() != null ? entity.getCategoria().name() : null)
                .nombreAula(entity.getAula() != null ? entity.getAula().getNombre() : "Desconocido")
                .nombreUsuario(entity.getUsuario() != null ? entity.getUsuario().getNombreUsuario() : "Desconocido")
                .fechaReporte(entity.getFechaReporte())
                .fechaCierre(entity.getFechaCierre())
                .adjuntoUrl(entity.getAdjuntoUrl())
                .build();
    }
}
