package iesalonsocano.gestiondeaverias.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que registra los movimientos de activos de inventario.
 * <p>
 * Cada movimiento captura el traslado de un activo de una ubicación (aula) a otra,
 * quién lo realizó, cuándo y el motivo. Permite mantener un historial completo
 * de la ubicación de cada activo.
 * </p>
 *
 * <p>
 * Tabla en BD: {@code movimientos_inventario}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
@Entity
@Table(name = "movimientos_inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioEntity {

    /**
     * Identificador único del movimiento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Activo que fue movido.
     */
    @NotNull(message = "El activo es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private InventarioEntity inventario;

    /**
     * Aula de origen (null si venía del almacén o sin ubicación).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_origen_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AulasEntity aulaOrigen;

    /**
     * Aula de destino (null si se mueve al almacén).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_destino_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AulasEntity aulaDestino;

    /**
     * Técnico o administrador que realizó el movimiento.
     */
    @NotNull(message = "El responsable del movimiento es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuariosEntity responsable;

    /**
     * Motivo o descripción del movimiento.
     */
    @Column(columnDefinition = "TEXT")
    private String motivo;

    /**
     * Fecha y hora en que se realizó el movimiento.
     */
    @Column(name = "fecha_movimiento", nullable = false, updatable = false)
    private LocalDateTime fechaMovimiento;

    @PrePersist
    protected void onCreate() {
        fechaMovimiento = LocalDateTime.now();
    }
}
