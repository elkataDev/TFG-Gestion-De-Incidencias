package iesalonsocano.gestiondeaverias.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "partes_trabajo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParteTrabajoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidencia_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private IncidenciasEntity incidencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuariosEntity tecnico;

    @Column(nullable = false)
    private Integer minutos;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "piezas_usadas", columnDefinition = "TEXT")
    private String piezasUsadas;

    @Column(precision = 10, scale = 2)
    private BigDecimal coste;

    @Column(name = "fecha_parte", nullable = false)
    private LocalDateTime fechaParte;

    @PrePersist
    protected void onCreate() {
        if (fechaParte == null) {
            fechaParte = LocalDateTime.now();
        }
    }
}
