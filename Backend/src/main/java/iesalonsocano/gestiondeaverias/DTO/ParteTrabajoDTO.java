package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Partes de Trabajo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParteTrabajoDTO {

    private Long id;
    private Long incidenciaId;
    private Long tecnicoId;
    private String nombreTecnico;
    private BigDecimal tiempoHoras;
    private String descripcionTrabajo;
    private String piezasUtilizadas;
    private LocalDateTime fechaRegistro;

    public static ParteTrabajoDTO fromEntity(ParteTrabajoEntity entity) {
        if (entity == null) return null;

        Long tecnicoId = null;
        String nombreTecnico = "Desconocido";
        if (entity.getTecnico() != null && Hibernate.isInitialized(entity.getTecnico())) {
            tecnicoId = entity.getTecnico().getId();
            nombreTecnico = entity.getTecnico().getNombreUsuario();
        }

        Long incidenciaId = null;
        if (entity.getIncidencia() != null && Hibernate.isInitialized(entity.getIncidencia())) {
            incidenciaId = entity.getIncidencia().getId();
        }

        return ParteTrabajoDTO.builder()
                .id(entity.getId())
                .incidenciaId(incidenciaId)
                .tecnicoId(tecnicoId)
                .nombreTecnico(nombreTecnico)
                .tiempoHoras(entity.getTiempoHoras())
                .descripcionTrabajo(entity.getDescripcionTrabajo())
                .piezasUtilizadas(entity.getPiezasUtilizadas())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }
}
