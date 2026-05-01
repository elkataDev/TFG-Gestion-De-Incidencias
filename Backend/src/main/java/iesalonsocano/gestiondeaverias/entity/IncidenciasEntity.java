package  iesalonsocano.gestiondeaverias.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una incidencia técnica reportada en el sistema.
 * <p>
 * Las incidencias son problemas técnicos reportados por usuarios (profesores)
 * que deben ser resueltos por el personal técnico.
 * </p>
 *
 * <p>
 * Incluye un sistema de estados para seguimiento del flujo de trabajo:
 * ABIERTO → EN_PROGRESO → RESUELTO
 * </p>
 *
 * <p>
 * Tabla en BD: {@code incidencias}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see EstadoIncidencia
 * @see CategoriaIncidencia
 */
@Entity
@Table(name = "incidencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidenciasEntity {

    /**
     * Identificador único de la incidencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título breve descriptivo de la incidencia.
     */
    @NotBlank(message = "El título de la incidencia es obligatorio")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    /**
     * Descripción detallada del problema.
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Estado actual de la incidencia.
     * @see EstadoIncidencia
     */
    @NotNull(message = "El estado de la incidencia es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoIncidencia estado;

    /**
     * Fecha y hora en que se reportó la incidencia.
     * <p>
     * Se establece automáticamente al crear la incidencia.
     * </p>
     */
    @Column(name = "fecha_reporte", nullable = false, updatable = false)
    private LocalDateTime fechaReporte;

    /**
     * Fecha y hora en que se cerró/resolvió la incidencia.
     * <p>
     * Se establece automáticamente cuando el estado cambia a RESUELTO.
     * </p>
     */
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    /**
     * Categoría de la incidencia.
     * @see CategoriaIncidencia
     */
    @NotNull(message = "La categoría es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoriaIncidencia categoria;

    /**
     * Usuario que reportó la incidencia.
     * <p>
     * Relación ManyToOne: Muchas incidencias pueden ser reportadas por un usuario.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuariosEntity usuario;

    /**
     * Aula donde ocurrió la incidencia.
     * <p>
     * Relación ManyToOne: Muchas incidencias pueden ocurrir en un aula.
     * Puede ser null si la incidencia no está asociada a un aula específica.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AulasEntity aula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private InventarioEntity activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_asignado_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuariosEntity tecnicoAsignado;

    /**
     * URL o identificador del archivo adjunto (opcional).
     */
    @Column(name = "adjunto_url")
    private String adjuntoUrl;

    /**
     * Método de callback JPA ejecutado antes de persistir una nueva incidencia.
     * <p>
     * Establece automáticamente:
     * <ul>
     *   <li>Fecha de reporte (ahora)</li>
     *   <li>Estado inicial (EN_PROGRESO si no se especificó)</li>
     * </ul>
     * </p>
     */
    @PrePersist
    protected void onCreate() {
        fechaReporte = LocalDateTime.now();
        if (estado == null) estado = EstadoIncidencia.ABIERTO;
    }

    /**
     * Método de callback JPA ejecutado antes de actualizar una incidencia.
     * <p>
     * Si el estado cambia a RESUELTO, establece automáticamente la fecha de cierre.
     * </p>
     */
    @PreUpdate
    protected void onUpdate() {
        if (estado == EstadoIncidencia.RESUELTO ) {
            fechaCierre = LocalDateTime.now();
        }
    }

    /**
     * Enumeración que define las categorías posibles de una incidencia.
     * <p>
     * Permite clasificar el tipo de problema técnico reportado.
     * </p>
     */
    public enum CategoriaIncidencia {
        /** Problemas relacionados con la red (conectividad, switches, routers) */
        RED,
        /** Problemas de software (aplicaciones, sistemas operativos) */
        SOFTWARE,
        /** Problemas de hardware (equipos físicos, componentes) */
        HARDWARE
    }


    /**
     * Enumeración que define los estados posibles del ciclo de vida de una incidencia.
     * <p>
     * Flujo típico: ABIERTO → EN_PROGRESO → RESUELTO
     * </p>
     */
    public enum EstadoIncidencia {
        /** Incidencia recién reportada, aún no asignada */
        ABIERTO,
        /** Incidencia en proceso de resolución */
        EN_PROGRESO,
        /** Incidencia bloqueada a la espera de acción externa */
        EN_ESPERA,
        /** Incidencia completamente resuelta */
        RESUELTO,
        /** Incidencia cerrada permanentemente */
        CERRADO,
        /** Incidencia reabierta tras cierre/resolución */
        REABIERTO
    }
}
