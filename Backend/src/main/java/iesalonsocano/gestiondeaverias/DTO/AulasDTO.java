package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulasDTO {
    private Long id;
    private String nombre;

    public static AulasDTO fromEntity(AulasEntity entity) {
        if (entity == null) return null;
        return AulasDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
}