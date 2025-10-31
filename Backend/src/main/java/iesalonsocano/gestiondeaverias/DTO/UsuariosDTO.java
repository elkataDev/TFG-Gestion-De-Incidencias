package iesalonsocano.gestiondeaverias.DTO;

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
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}