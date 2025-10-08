package com.tuempresa.tuapp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidenciasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título de la incidencia es obligatorio")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El estado de la incidencia es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoIncidencia estado;

    @Column(name = "fecha_reporte", nullable = false, updatable = false)
    private LocalDateTime fechaReporte;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    // Relación con Usuario (quien reporta la incidencia)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuariosEntity usuario;

    // Relación con Aula (dónde ocurrió la incidencia)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    private AulasEntity aula;

    //Fecha de creacion de incidencia
    @PrePersist
    protected void onCreate() {
        fechaReporte = LocalDateTime.now();
        if (estado == null) estado = EstadoIncidencia.PENDIENTE;
    }

    //Fecha de cierre actual cuando el estado de la incidencia este resuelto o cancelada
    @PreUpdate
    protected void onUpdate() {
        if (estado == EstadoIncidencia.RESUELTA || estado == EstadoIncidencia.CANCELADA) {
            fechaCierre = LocalDateTime.now();
        }
    }

    // Enum para los estados posibles de la incidencia
    public enum EstadoIncidencia {
        PENDIENTE,
        EN_PROCESO,
        RESUELTA,
        CANCELADA
    }
}