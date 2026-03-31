package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HistorialEstadoDTO {
    private Long id;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;
    private String nombreUsuario;

    public static HistorialEstadoDTO fromEntity(HistorialEstadoEntity entity) {
        if (entity == null) return null;

        String nombreUsuario = "Sistema";
        if (entity.getUsuario() != null && Hibernate.isInitialized(entity.getUsuario())) {
            nombreUsuario = entity.getUsuario().getNombreUsuario();
        }

        return HistorialEstadoDTO.builder()
                .id(entity.getId())
                .estadoAnterior(entity.getEstadoAnterior() != null ? entity.getEstadoAnterior().name() : null)
                .estadoNuevo(entity.getEstadoNuevo() != null ? entity.getEstadoNuevo().name() : null)
                .fechaCambio(entity.getFechaCambio())
                .nombreUsuario(nombreUsuario)
                .build();
    }
}
