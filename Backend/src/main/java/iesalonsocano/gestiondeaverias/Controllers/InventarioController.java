 package iesalonsocano.gestiondeaverias.controller;

import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos los artículos del inventario
    @GetMapping
    public List<InventarioEntity> getAllInventario() {
        return inventarioService.findAll();
    }

    // Obtener un artículo por su ID
    @GetMapping("/{id}")
    public ResponseEntity<InventarioEntity> getInventarioById(@PathVariable Long id) {
        Optional<InventarioEntity> optionalItem = inventarioService.findById(id);

        if (optionalItem.isPresent()) {
            return ResponseEntity.ok(optionalItem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un nuevo artículo
    @PostMapping
    public ResponseEntity<InventarioEntity> createInventario(@Valid @RequestBody InventarioEntity inventario) {
        InventarioEntity nuevoItem = inventarioService.save(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem);
    }

    // Actualizar un artículo existente
    @PutMapping("/{id}")
    public ResponseEntity<InventarioEntity> updateInventario(@PathVariable Long id, @Valid @RequestBody InventarioEntity inventarioDetails) {
        Optional<InventarioEntity> optionalItem = inventarioService.findById(id);

        if (optionalItem.isPresent()) {
            InventarioEntity itemExistente = optionalItem.get();

            itemExistente.setNombre(inventarioDetails.getNombre());
            itemExistente.setDescripcion(inventarioDetails.getDescripcion());
            itemExistente.setCodigo_QR(inventarioDetails.getCodigo_QR());
            itemExistente.setEstado(inventarioDetails.getEstado());
            itemExistente.setAula(inventarioDetails.getAula());

            InventarioEntity actualizado = inventarioService.save(itemExistente);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un artículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        Optional<InventarioEntity> optionalItem = inventarioService.findById(id);

        if (optionalItem.isPresent()) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}