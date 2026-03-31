package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.ComentarioEntity;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ComentarioDTO {
    private Long id;
    private String texto;
    private LocalDateTime fecha;
    private Long usuarioId;
    private String nombreUsuario;

    public static ComentarioDTO fromEntity(ComentarioEntity entity) {
        if (entity == null) return null;

        Long usuarioId = null;
        String nombreUsuario = "Desconocido";
        if (entity.getUsuario() != null && Hibernate.isInitialized(entity.getUsuario())) {
            usuarioId = entity.getUsuario().getId();
            nombreUsuario = entity.getUsuario().getNombreUsuario();
        }

        return ComentarioDTO.builder()
                .id(entity.getId())
                .texto(entity.getTexto())
                .fecha(entity.getFecha())
                .usuarioId(usuarioId)
                .nombreUsuario(nombreUsuario)
                .build();
    }
}
