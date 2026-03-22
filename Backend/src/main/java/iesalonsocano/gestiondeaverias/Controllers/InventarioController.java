package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.InventarioDTO;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.Services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión del inventario de equipamiento informático.
 * <p>
 * Proporciona endpoints para realizar operaciones CRUD sobre el inventario
 * de equipos del centro educativo (computadoras, impresoras, proyectores, etc.).
 * </p>
 *
 * <p>
 * Seguridad:
 * <ul>
 *   <li>GET - Accesible para cualquier usuario autenticado</li>
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
 * @version 1.0.0
 * @see InventarioService
 * @see InventarioDTO
 */
@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost"})
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    /**
     * Obtiene la lista completa de artículos del inventario.
     *
     * @return ResponseEntity con lista de InventarioDTO
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
     *
     * @param id identificador del artículo
     * @return ResponseEntity con InventarioDTO si existe, o 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> getInventarioById(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(item -> ResponseEntity.ok(InventarioDTO.fromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo artículo en el inventario.
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param inventario entidad con los datos del artículo
     * @return ResponseEntity con InventarioDTO creado y código HTTP 201 Created
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<InventarioDTO> createInventario(@Valid @RequestBody InventarioEntity inventario) {
        InventarioEntity nuevoItem = inventarioService.save(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(InventarioDTO.fromEntity(nuevoItem));
    }

    /**
     * Actualiza los datos de un artículo del inventario.
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param id identificador del artículo
     * @param details entidad con los nuevos datos
     * @return ResponseEntity con InventarioDTO actualizado o 404 Not Found
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<InventarioDTO> updateInventario(@PathVariable Long id, @Valid @RequestBody InventarioEntity details) {
        return inventarioService.findById(id)
                .map(item -> {
                    item.setNombre(details.getNombre());
                    item.setDescripcion(details.getDescripcion());
                    item.setCodigoQR(details.getCodigoQR());
                    item.setEstado(details.getEstado());
                    item.setAula(details.getAula());

                    InventarioEntity actualizado = inventarioService.save(item);
                    return ResponseEntity.ok(InventarioDTO.fromEntity(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un artículo del inventario.
     * <p>
     * Acceso: Solo ADMIN
     * </p>
     *
     * @param id identificador del artículo a eliminar
     * @return ResponseEntity vacío con código 204 No Content o 404 Not Found
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
}