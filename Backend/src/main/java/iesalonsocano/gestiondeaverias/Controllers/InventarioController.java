package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.Services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario") // Añadido /api por consistencia con tu SecurityConfig
@CrossOrigin(origins = "http://localhost:5173") // Para permitir conexión desde React (Vite)
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos: Visible para cualquier usuario autenticado (para reportar averías)
    @GetMapping
    public List<InventarioEntity> getAllInventario() {
        return inventarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioEntity> getInventarioById(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear: Solo Administrador o Técnico TIC
    @PostMapping
    @PreAuthorize("hasAnyRole('administrador', 'tecnico')")
    public ResponseEntity<InventarioEntity> createInventario(@Valid @RequestBody InventarioEntity inventario) {
        // El servicio debería generar el código QR aquí antes de guardar
        InventarioEntity nuevoItem = inventarioService.save(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem);
    }

    // Actualizar: Solo Administrador o Técnico TIC
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('administrador', 'tecnico')")
    public ResponseEntity<InventarioEntity> updateInventario(@PathVariable Long id, @Valid @RequestBody InventarioEntity inventarioDetails) {
        Optional<InventarioEntity> optionalItem = inventarioService.findById(id);

        if (optionalItem.isPresent()) {
            InventarioEntity itemExistente = optionalItem.get();

            // Actualización de campos según Requisito 1.3.1
            itemExistente.setNombre(inventarioDetails.getNombre());
            itemExistente.setDescripcion(inventarioDetails.getDescripcion());
            itemExistente.setCodigoQR(inventarioDetails.getCodigoQR());
            itemExistente.setEstado(inventarioDetails.getEstado());
            itemExistente.setAula(inventarioDetails.getAula());

            return ResponseEntity.ok(inventarioService.save(itemExistente));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar: Solo Administrador
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('administrador')")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        if (inventarioService.findById(id).isPresent()) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}