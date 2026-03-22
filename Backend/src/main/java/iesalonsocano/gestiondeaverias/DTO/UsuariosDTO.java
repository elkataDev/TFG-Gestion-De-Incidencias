package iesalonsocano.gestiondeaverias.DTO;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para transferir información de usuarios.
 * <p>
 * Excluye campos sensibles como la contraseña para seguridad.
 * Se utiliza en respuestas de la API para evitar exponer datos confidenciales.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see UsuariosEntity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuariosDTO {
    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Nombre de usuario para login.
     */
    private String nombreUsuario;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Rol del usuario en el sistema (USER, ADMIN, TECNICO).
     */
    private String rol;

    /**
     * Indica si el usuario está activo en el sistema.
     */
    private Boolean activo;

    /**
     * Fecha de creación de la cuenta del usuario.
     */
    private LocalDateTime fechaCreacion;

    /**
     * Convierte una entidad UsuariosEntity a su correspondiente DTO.
     * <p>
     * Nota: La contraseña NO se incluye en el DTO por seguridad.
     * </p>
     *
     * @param entity entidad de usuario a convertir
     * @return UsuariosDTO con los datos de la entidad (sin contraseña), o null si la entidad es null
     */
    public static UsuariosDTO fromEntity(UsuariosEntity entity) {
        if (entity == null) return null;
        return UsuariosDTO.builder()
                .id(entity.getId())
                .nombreUsuario(entity.getNombreUsuario())
                .email(entity.getEmail())
                .rol(entity.getRol() != null ? entity.getRol() : null)
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}