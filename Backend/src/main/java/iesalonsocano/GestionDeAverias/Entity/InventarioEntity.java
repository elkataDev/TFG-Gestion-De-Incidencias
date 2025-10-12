package com.tuempresa.tuapp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del artículo es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    @Size(max = 50, message = "El código de inventario no puede tener más de 50 caracteres")
    @Column(name = "codigo_QR", unique = true, length = 50)
    private String codigo_QR;

    @Column(name = "estado", length = 50)
    private estadoInventario estado;

    @Column(name = "fecha_ingreso", nullable = false, updatable = false)
    private LocalDateTime fechaIngreso;

    // Relación con Aula
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    private AulasEntity aula;

    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
    }

    public enum estadoInventario {
        DISPONIBLE,
        EN_USO,
        DAÑADO
    }
}
 /*nombre, cantidad, codigoInventario: datos principales de cada ítem del inventario.

estado: útil para marcar si el objeto está disponible, prestado o dañado.

aula: relación @ManyToOne hacia AulasEntity, para saber a qué aula pertenece (opcional).

fechaIngreso : gestionadas automáticamente con @PrePersist.*/