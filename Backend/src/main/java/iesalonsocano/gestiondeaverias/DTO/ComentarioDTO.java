package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.ComentarioEntity;
import lombok.*;

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
        return ComentarioDTO.builder()
                .id(entity.getId())
                .texto(entity.getTexto())
                .fecha(entity.getFecha())
                .usuarioId(entity.getUsuario() != null ? entity.getUsuario().getId() : null)
                .nombreUsuario(entity.getUsuario() != null ? entity.getUsuario().getNombreUsuario() : "Desconocido")
                .build();
    }
}
