package iesalonsocano.gestiondeaverias.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un usuario del sistema.
 * <p>
 * Los usuarios pueden tener diferentes roles (USER, ADMIN, TECNICO) y son
 * responsables de reportar incidencias y gestionar el sistema.
 * </p>
 *
 * <p>
 * La contraseña se almacena cifrada con BCrypt para seguridad.
 * </p>
 *
 * <p>
 * Tabla en BD: {@code usuarios}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuariosEntity {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario para login.
     * <p>
     * Debe ser único en el sistema.
     * </p>
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres")
    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    /**
     * Correo electrónico del usuario.
     * <p>
     * Debe ser único y válido.
     * </p>
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Contraseña cifrada con BCrypt.
     * <p>
     * No se debe almacenar en texto plano. El cifrado se realiza
     * automáticamente en el servicio de usuarios.
     * </p>
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario en el sistema.
     * <p>
     * Valores posibles:
     * <ul>
     *   <li>USUARIO - Usuario estándar (profesores)</li>
     *   <li>TECNICO - Personal técnico</li>
     *   <li>ADMIN - Administrador del sistema</li>
     * </ul>
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    @Builder.Default
    private RolUsuario rol = RolUsuario.USUARIO;

    /**
     * Indica si el usuario está activo en el sistema.
     * <p>
     * Los usuarios inactivos no pueden acceder al sistema.
     * Se utiliza en lugar de eliminar usuarios para mantener el historial.
     * </p>
     */
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /**
     * Fecha y hora de creación de la cuenta del usuario.
     * <p>
     * Se establece automáticamente al crear el usuario.
     * </p>
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Método de callback JPA ejecutado antes de persistir un nuevo usuario.
     * <p>
     * Establece automáticamente la fecha de creación.
     * </p>
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (rol == null) {
            rol = RolUsuario.USUARIO;
        }
    }

    public enum RolUsuario {
        USUARIO,
        TECNICO,
        ADMIN
    }
}
