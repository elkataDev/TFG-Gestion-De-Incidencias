package iesalonsocano.gestiondeaverias.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estados")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HistorialEstadoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private IncidenciasEntity.EstadoIncidencia estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private IncidenciasEntity.EstadoIncidencia estadoNuevo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidencia_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private IncidenciasEntity incidencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true) 
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuariosEntity usuario;

    @PrePersist
    protected void onCreate() {
        fechaCambio = LocalDateTime.now();
    }
}
