package  iesalonsocano.gestiondeaverias.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un aula o espacio del centro educativo.
 * <p>
 * Las aulas son contenedores de equipamiento (inventario) y lugares donde
 * ocurren las incidencias técnicas.
 * </p>
 *
 * <p>
 * Tabla en BD: {@code aulas}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
@Entity
@Table(name = "aulas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulasEntity {

    /**
     * Identificador único del aula.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del aula.
     * <p>
     * Debe ser único en el sistema.
     * Ejemplos: "Aula 101", "Laboratorio de Informática", "Sala de Profesores"
     * </p>
     */
    @NotBlank(message = "El nombre del aula es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
}