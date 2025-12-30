package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidenciasDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaReporte;
    private LocalDateTime fechaCierre;

    // --- DATOS DEL AULA ---
    private Long aulaId;


    // --- DATOS DEL USUARIO ---
    private Long usuarioId;
    private String nombreUsuario; // Muy útil para mostrarlo en la tabla directamente

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
                .fechaReporte(entity.getFechaReporte())
                .fechaCierre(entity.getFechaCierre())

                // Mapeo seguro del Aula
                .aulaId(entity.getAula() != null ? entity.getAula().getId() : null)

                // Mapeo del Usuario (asumiendo que usuario nunca es null por la BD, pero protegemos igual)
                .usuarioId(entity.getUsuario() != null ? entity.getUsuario().getId() : null)

                .nombreUsuario(entity.getUsuario() != null ? entity.getUsuario().getNombreUsuario() : "Desconocido")
                .build();
    }
}