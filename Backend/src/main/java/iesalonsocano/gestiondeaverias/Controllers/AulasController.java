package iesalonsocano.gestiondeaverias.controller;

import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import iesalonsocano.gestiondeaverias.service.AulasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aulas")
public class AulasController {

    @Autowired
    private AulasService aulasService;

    // Obtener todas las aulas
    @GetMapping
    public List<AulasEntity> getAllAulas() {
        return aulasService.findAll();
    }

    // Obtener un aula por su ID
    @GetMapping("/{id}")
    public ResponseEntity<AulasEntity> getAulaById(@PathVariable Long id) {
        Optional<AulasEntity> optionalAula = aulasService.findById(id);

        if (optionalAula.isPresent()) {
            AulasEntity aula = optionalAula.get();
            return ResponseEntity.ok(aula);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear una nueva aula
    @PostMapping
    public ResponseEntity<AulasEntity> createAula(@Valid @RequestBody AulasEntity aula) {
        AulasEntity nuevaAula = aulasService.save(aula);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAula);
    }

    // Actualizar un aula existente
    @PutMapping("/{id}")
    public ResponseEntity<AulasEntity> updateAula(@PathVariable Long id, @Valid @RequestBody AulasEntity aulaDetails) {
        Optional<AulasEntity> optionalAula = aulasService.findById(id);

        if (optionalAula.isPresent()) {
            AulasEntity aulaExistente = optionalAula.get();
            aulaExistente.setNombre(aulaDetails.getNombre());

            AulasEntity aulaActualizada = aulasService.save(aulaExistente);
            return ResponseEntity.ok(aulaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un aula
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAula(@PathVariable Long id) {
        Optional<AulasEntity> optionalAula = aulasService.findById(id);

        if (optionalAula.isPresent()) {
            aulasService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}