package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import lombok.*;

/**
 * DTO (Data Transfer Object) para transferir información de aulas.
 * <p>
 * Se utiliza para exponer datos de aulas en las respuestas de la API,
 * evitando exponer la entidad JPA directamente y previniendo problemas
 * de serialización con relaciones lazy.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see AulasEntity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulasDTO {
    /**
     * Identificador único del aula.
     */
    private Long id;

    /**
     * Nombre del aula (ej: "Aula 101", "Laboratorio de Informática").
     */
    private String nombre;

    /**
     * Convierte una entidad AulasEntity a su correspondiente DTO.
     *
     * @param entity entidad de aula a convertir
     * @return AulasDTO con los datos de la entidad, o null si la entidad es null
     */
    public static AulasDTO fromEntity(AulasEntity entity) {
        if (entity == null) return null;
        return AulasDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
}