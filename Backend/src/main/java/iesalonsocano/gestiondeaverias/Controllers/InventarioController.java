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

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:5173")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioDTO>> getAllInventario() {
        List<InventarioDTO> dtos = inventarioService.findAll().stream()
                .map(InventarioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> getInventarioById(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(item -> ResponseEntity.ok(InventarioDTO.fromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<InventarioDTO> createInventario(@Valid @RequestBody InventarioEntity inventario) {
        InventarioEntity nuevoItem = inventarioService.save(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(InventarioDTO.fromEntity(nuevoItem));
    }

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