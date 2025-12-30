package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String codigoQR;
    private String estado;
    private LocalDateTime fechaIngreso;
    private Long aulaId;
    private String nombreAula;

    public static InventarioDTO fromEntity(InventarioEntity entity) {
        if (entity == null) return null;
        return InventarioDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .codigoQR(entity.getCodigoQR())
                .estado(entity.getEstado() != null ? entity.getEstado().name() : null)
                .fechaIngreso(entity.getFechaIngreso())
                .aulaId(entity.getAula() != null ? entity.getAula().getId() : null)
                .nombreAula(entity.getAula() != null ? entity.getAula().getNombre() : "Sin asignar")
                .build();
    }
}