package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParteTrabajoDTO {
    private Long id;
    private Integer minutos;
    private String descripcion;
    private String piezasUsadas;
    private BigDecimal coste;
    private LocalDateTime fechaParte;
    private Long tecnicoId;
    private String nombreTecnico;

    public static ParteTrabajoDTO fromEntity(ParteTrabajoEntity entity) {
        if (entity == null) return null;

        Long tecnicoId = null;
        String nombreTecnico = "Desconocido";
        if (entity.getTecnico() != null && Hibernate.isInitialized(entity.getTecnico())) {
            tecnicoId = entity.getTecnico().getId();
            nombreTecnico = entity.getTecnico().getNombreUsuario();
        }

        return ParteTrabajoDTO.builder()
                .id(entity.getId())
                .minutos(entity.getMinutos())
                .descripcion(entity.getDescripcion())
                .piezasUsadas(entity.getPiezasUsadas())
                .coste(entity.getCoste())
                .fechaParte(entity.getFechaParte())
                .tecnicoId(tecnicoId)
                .nombreTecnico(nombreTecnico)
                .build();
    }
}
