package  iesalonsocano.gestiondeaverias.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un artículo del inventario de equipamiento informático.
 * <p>
 * Incluye equipos como computadoras, impresoras, proyectores, servidores, etc.
 * Cada artículo puede tener un código QR para identificación rápida y estar
 * asignado a un aula específica.
 * </p>
 *
 * <p>
 * Tabla en BD: {@code inventario}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see EstadoInventario
 * @see CategoriaInventario
 */
@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioEntity {

    /**
     * Identificador único del artículo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre descriptivo del artículo.
     * <p>
     * Ejemplos: "PC-101", "Impresora HP LaserJet Pro", "Proyector Epson"
     * </p>
     */
    @NotBlank(message = "El nombre del artículo es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Descripción detallada del artículo.
     */
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    /**
     * Código QR único del artículo para escaneo rápido.
     */
    @Size(max = 50, message = "El código de inventario no puede tener más de 50 caracteres")
    @Column(name = "codigo_QR", unique = true, length = 50)
    private String codigoQR;

    /**
     * Estado actual del artículo.
     * @see EstadoInventario
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50)
    private EstadoInventario estado;

    /**
     * Fecha y hora de ingreso del artículo al inventario.
     * <p>
     * Se establece automáticamente al crear el registro.
     * </p>
     */
    @Column(name = "fecha_ingreso", nullable = false, updatable = false)
    private LocalDateTime fechaIngreso;

    /**
     * Marca del fabricante del activo (ej: HP, Dell, Epson).
     */
    @Size(max = 100)
    @Column(length = 100)
    private String marca;

    /**
     * Modelo del activo (ej: LaserJet Pro M404, Latitude 5500).
     */
    @Size(max = 100)
    @Column(length = 100)
    private String modelo;

    /**
     * Número de serie único del activo físico.
     */
    @Size(max = 100)
    @Column(name = "numero_serie", unique = true, length = 100)
    private String numeroSerie;

    /**
     * Categoría del artículo.
     * @see CategoriaInventario
     */
    @NotNull(message = "La categoría es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaInventario categoria;

    /**
     * Aula donde se encuentra ubicado el artículo.
     * <p>
     * Relación ManyToOne: Muchos artículos pueden estar en un aula.
     * Puede ser null si el artículo está en almacén.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AulasEntity aula;

    /**
     * Método de callback JPA ejecutado antes de persistir un nuevo artículo.
     * <p>
     * Establece automáticamente la fecha de ingreso.
     * </p>
     */
    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
    }

    /**
     * Enumeración que define los estados posibles de un artículo del inventario.
     */
    public enum EstadoInventario {
        /** Artículo disponible para uso */
        DISPONIBLE,
        /** Artículo actualmente en uso */
        EN_USO,
        /** Artículo dañado, requiere reparación o reemplazo */
        DAÑADO
    }

    /**
     * Enumeración que define las categorías de equipamiento informático.
     * <p>
     * Permite clasificar los artículos del inventario por tipo de equipo.
     * </p>
     */
    public enum CategoriaInventario {
        /** Computadoras de escritorio o portátiles */
        COMPUTADORA,
        /** Impresoras (láser, inyección de tinta, multifunción) */
        IMPRESORA,
        /** Proyectores o cañones */
        PROYECTOR,
        /** Monitores o pantallas */
        MONITOR,
        /** Equipamiento de red (switches, routers, access points) */
        RED,
        /** Servidores */
        SERVIDOR,
        /** Periféricos (teclados, ratones, webcams, etc.) */
        PERIFERICO,
        /** Equipamiento de seguridad (cámaras, sistemas de acceso) */
        SEGURIDAD,
        /** Licencias de software */
        LICENCIA,
        /** Consumibles (tinta, papel, toner, cables, etc.) */
        CONSUMIBLE
    }
}