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
    private String categoria; // <-- Enum como String
    private String nombreAula; // <-- nombre del aula
    private String nombreUsuario; // <-- nombre del usuario
    private LocalDateTime fechaReporte;
    private LocalDateTime fechaCierre;

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
                .build();
    }
}
