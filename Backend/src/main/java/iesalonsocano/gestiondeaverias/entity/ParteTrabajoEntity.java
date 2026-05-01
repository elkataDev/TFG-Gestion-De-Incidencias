package iesalonsocano.gestiondeaverias.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un Parte de Trabajo asociado a una incidencia.
 * <p>
 * Cada parte registra el tiempo invertido por un técnico en resolver una incidencia
 * y las piezas o materiales utilizados. Una incidencia puede tener múltiples partes
 * (ej: visita inicial + visita de seguimiento).
 * </p>
 *
 * <p>
 * Tabla en BD: {@code partes_trabajo}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
@Entity
@Table(name = "partes_trabajo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParteTrabajoEntity {

    /**
     * Identificador único del parte de trabajo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Incidencia a la que pertenece este parte de trabajo.
     */
    @NotNull(message = "La incidencia es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidencia_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private IncidenciasEntity incidencia;

    /**
     * Técnico que realizó el trabajo.
     */
    @NotNull(message = "El técnico es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuariosEntity tecnico;

    /**
     * Tiempo invertido en horas (puede ser decimal, ej: 1.5 = 90 minutos).
     */
    @NotNull(message = "El tiempo invertido es obligatorio")
    @DecimalMin(value = "0.1", message = "El tiempo mínimo es 0.1 horas (6 minutos)")
    @Column(name = "tiempo_horas", nullable = false, precision = 5, scale = 2)
    private BigDecimal tiempoHoras;

    /**
     * Descripción de las tareas realizadas.
     */
    @Column(name = "descripcion_trabajo", columnDefinition = "TEXT")
    private String descripcionTrabajo;

    /**
     * Lista de piezas o materiales utilizados durante la intervención.
     * Formato libre (ej: "2x Memoria RAM DDR4 8GB, 1x Cable HDMI").
     */
    @Column(name = "piezas_utilizadas", columnDefinition = "TEXT")
    private String piezasUtilizadas;

    /**
     * Fecha y hora de registro del parte.
     */
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
