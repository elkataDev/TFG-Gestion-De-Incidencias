package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.InventarioDTO;
import iesalonsocano.gestiondeaverias.DTO.MovimientoInventarioDTO;
import iesalonsocano.gestiondeaverias.Services.AulasService;
import iesalonsocano.gestiondeaverias.Services.InventarioService;
import iesalonsocano.gestiondeaverias.Services.MovimientoInventarioService;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;
import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.entity.MovimientoInventarioEntity;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión del inventario de equipamiento informático.
 * <p>
 * Proporciona endpoints CRUD sobre el inventario, así como el historial de
 * movimientos de cada activo entre ubicaciones.
 * </p>
 *
 * <p>
 * Seguridad:
 * <ul>
 *   <li>GET - Accesible para TECNICO y ADMIN</li>
 *   <li>POST, PUT - Solo ADMIN o TECNICO</li>
 *   <li>DELETE - Solo ADMIN</li>
 * </ul>
 * </p>
 *
 * <p>
 * Base URL: {@code /api/inventario}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 2.0.0
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    @Autowired
    private AulasService aulasService;

    @Autowired
    private UsuariosService usuariosService;

    /**
     * Obtiene la lista completa de artículos del inventario.
     */
    @GetMapping
    public ResponseEntity<List<InventarioDTO>> getAllInventario() {
        List<InventarioDTO> dtos = inventarioService.findAll().stream()
                .map(InventarioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene un artículo específico del inventario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> getInventarioById(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(item -> ResponseEntity.ok(InventarioDTO.fromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo artículo en el inventario.
     * Si se especifica aulaId, el activo se asigna al aula indicada.
     * Acceso: Solo ADMIN o TECNICO.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<?> createInventario(
            @Valid @RequestBody Map<String, Object> body,
            Principal principal) {
        try {
            InventarioEntity item = buildInventarioFromBody(body);
            InventarioEntity saved = inventarioService.save(item);

            // Registrar movimiento de entrada si tiene aula asignada
            if (saved.getAula() != null && principal != null) {
                registrarMovimiento(saved, null, saved.getAula(), principal, "Alta de activo en inventario");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(InventarioDTO.fromEntity(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un artículo del inventario.
     * Si cambia el aula, registra automáticamente un movimiento.
     * Acceso: Solo ADMIN o TECNICO.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<?> updateInventario(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Principal principal) {
        return inventarioService.findById(id)
                .map(item -> {
                    AulasEntity aulaAnterior = item.getAula();

                    // Actualizar campos básicos
                    if (body.get("nombre") != null) item.setNombre(body.get("nombre").toString());
                    if (body.get("descripcion") != null) item.setDescripcion(body.get("descripcion").toString());
                    if (body.get("codigoQR") != null) item.setCodigoQR(body.get("codigoQR").toString());
                    if (body.get("marca") != null) item.setMarca(body.get("marca").toString());
                    if (body.get("modelo") != null) item.setModelo(body.get("modelo").toString());
                    if (body.get("numeroSerie") != null) item.setNumeroSerie(body.get("numeroSerie").toString());

                    // Estado
                    if (body.get("estado") != null) {
                        item.setEstado(InventarioEntity.EstadoInventario.valueOf(body.get("estado").toString().toUpperCase()));
                    }
                    // Categoría
                    if (body.get("categoria") != null) {
                        item.setCategoria(InventarioEntity.CategoriaInventario.valueOf(body.get("categoria").toString().toUpperCase()));
                    }

                    // Cambio de ubicación (genera movimiento)
                    AulasEntity aulaNueva = aulaAnterior;
                    if (body.containsKey("aulaId")) {
                        Object aulaIdObj = body.get("aulaId");
                        if (aulaIdObj == null) {
                            aulaNueva = null;
                        } else {
                            Long aulaId = Long.parseLong(aulaIdObj.toString());
                            aulaNueva = aulasService.findById(aulaId).orElse(null);
                        }
                        item.setAula(aulaNueva);
                    }

                    InventarioEntity actualizado = inventarioService.save(item);

                    // Registrar movimiento si cambió el aula
                    boolean aulaChanged = (aulaAnterior == null && aulaNueva != null)
                            || (aulaAnterior != null && !aulaAnterior.equals(aulaNueva));
                    if (aulaChanged && principal != null) {
                        String motivo = body.get("motivoMovimiento") != null
                                ? body.get("motivoMovimiento").toString()
                                : "Traslado de activo";
                        registrarMovimiento(actualizado, aulaAnterior, aulaNueva, principal, motivo);
                    }

                    return ResponseEntity.ok(InventarioDTO.fromEntity(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un artículo del inventario.
     * Acceso: Solo ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        if (inventarioService.findById(id).isPresent()) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtiene el historial de movimientos de un activo.
     * Acceso: TECNICO y ADMIN.
     */
    @GetMapping("/{id}/movimientos")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<List<MovimientoInventarioDTO>> getMovimientos(@PathVariable Long id) {
        List<MovimientoInventarioDTO> dtos = movimientoInventarioService.findByInventarioId(id)
                .stream()
                .map(MovimientoInventarioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Registra manualmente un movimiento de activo (traslado entre aulas).
     * Acceso: TECNICO y ADMIN.
     */
    @PostMapping("/{id}/movimientos")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<?> registrarMovimientoManual(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Principal principal) {
        InventarioEntity activo = inventarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Activo no encontrado"));

        AulasEntity aulaOrigen = activo.getAula();
        AulasEntity aulaDestino = null;

        if (body.get("aulaDestinoId") != null) {
            Long aulaDestinoId = Long.parseLong(body.get("aulaDestinoId").toString());
            aulaDestino = aulasService.findById(aulaDestinoId).orElse(null);
        }

        String motivo = body.get("motivo") != null ? body.get("motivo").toString() : "Traslado manual";

        // Actualizar ubicación del activo
        activo.setAula(aulaDestino);
        inventarioService.save(activo);

        // Registrar el movimiento
        MovimientoInventarioEntity movimiento = registrarMovimiento(activo, aulaOrigen, aulaDestino, principal, motivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(MovimientoInventarioDTO.fromEntity(movimiento));
    }

    // ======== HELPERS PRIVADOS ========

    private InventarioEntity buildInventarioFromBody(Map<String, Object> body) {
        InventarioEntity item = new InventarioEntity();

        if (body.get("nombre") == null) throw new IllegalArgumentException("El campo 'nombre' es obligatorio");
        item.setNombre(body.get("nombre").toString());

        if (body.get("categoria") == null) throw new IllegalArgumentException("El campo 'categoria' es obligatorio");
        item.setCategoria(InventarioEntity.CategoriaInventario.valueOf(body.get("categoria").toString().toUpperCase()));

        if (body.get("descripcion") != null) item.setDescripcion(body.get("descripcion").toString());
        if (body.get("codigoQR") != null) item.setCodigoQR(body.get("codigoQR").toString());
        if (body.get("marca") != null) item.setMarca(body.get("marca").toString());
        if (body.get("modelo") != null) item.setModelo(body.get("modelo").toString());
        if (body.get("numeroSerie") != null) item.setNumeroSerie(body.get("numeroSerie").toString());

        if (body.get("estado") != null) {
            item.setEstado(InventarioEntity.EstadoInventario.valueOf(body.get("estado").toString().toUpperCase()));
        } else {
            item.setEstado(InventarioEntity.EstadoInventario.DISPONIBLE);
        }

        if (body.get("aulaId") != null) {
            Long aulaId = Long.parseLong(body.get("aulaId").toString());
            AulasEntity aula = aulasService.findById(aulaId)
                    .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada con id: " + aulaId));
            item.setAula(aula);
        }

        return item;
    }

    private MovimientoInventarioEntity registrarMovimiento(
            InventarioEntity activo,
            AulasEntity aulaOrigen,
            AulasEntity aulaDestino,
            Principal principal,
            String motivo) {
        UsuariosEntity responsable = usuariosService.findByNombreUsuario(principal.getName()).orElse(null);
        MovimientoInventarioEntity movimiento = MovimientoInventarioEntity.builder()
                .inventario(activo)
                .aulaOrigen(aulaOrigen)
                .aulaDestino(aulaDestino)
                .responsable(responsable)
                .motivo(motivo)
                .build();
        return movimientoInventarioService.save(movimiento);
    }
}