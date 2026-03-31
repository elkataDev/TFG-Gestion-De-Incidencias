package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuariosDTO {
    private Long id;
    private String nombreUsuario;
    private String email;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    public static UsuariosDTO fromEntity(UsuariosEntity entity) {
        if (entity == null) return null;
        return UsuariosDTO.builder()
                .id(entity.getId())
                .nombreUsuario(entity.getNombreUsuario())
                .email(entity.getEmail())
                .rol(entity.getRol() != null ? entity.getRol().name() : null)
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}