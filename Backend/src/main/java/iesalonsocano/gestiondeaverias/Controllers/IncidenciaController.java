package iesalonsocano.gestiondeaverias.controller;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.service.IncidenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciasService incidenciasService;

    // Obtener todas las incidencias
    @GetMapping
    public List<IncidenciasEntity> getAllIncidencias() {
        return incidenciasService.findAll();
    }

    // Obtener una incidencia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<IncidenciasEntity> getIncidenciaById(@PathVariable Long id) {
        Optional<IncidenciasEntity> optionalIncidencia = incidenciasService.findById(id);

        if (optionalIncidencia.isPresent()) {
            return ResponseEntity.ok(optionalIncidencia.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear una nueva incidencia
    @PostMapping
    public ResponseEntity<IncidenciasEntity> createIncidencia(@Valid @RequestBody IncidenciasEntity incidencia) {
        IncidenciasEntity nuevaIncidencia = incidenciasService.save(incidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaIncidencia);
    }

    // Actualizar una incidencia existente
    @PutMapping("/{id}")
    public ResponseEntity<IncidenciasEntity> updateIncidencia(@PathVariable Long id, @Valid @RequestBody IncidenciasEntity incidenciaDetails) {
        Optional<IncidenciasEntity> optionalIncidencia = incidenciasService.findById(id);

        if (optionalIncidencia.isPresent()) {
            IncidenciasEntity incidenciaExistente = optionalIncidencia.get();

            incidenciaExistente.setTitulo(incidenciaDetails.getTitulo());
            incidenciaExistente.setDescripcion(incidenciaDetails.getDescripcion());
            incidenciaExistente.setEstado(incidenciaDetails.getEstado());
            incidenciaExistente.setUsuario(incidenciaDetails.getUsuario());
            incidenciaExistente.setAula(incidenciaDetails.getAula());

            IncidenciasEntity actualizada = incidenciasService.save(incidenciaExistente);
            return ResponseEntity.ok(actualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una incidencia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncidencia(@PathVariable Long id) {
        Optional<IncidenciasEntity> optionalIncidencia = incidenciasService.findById(id);

        if (optionalIncidencia.isPresent()) {
            incidenciasService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}