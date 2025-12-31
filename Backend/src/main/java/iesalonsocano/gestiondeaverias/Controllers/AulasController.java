package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.AulasDTO;
import iesalonsocano.gestiondeaverias.Services.IncidenciasService;
import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import iesalonsocano.gestiondeaverias.Services.AulasService;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/aulas")
@CrossOrigin(origins = "http://localhost:5173")
public class AulasController {

    @Autowired
    private AulasService aulasService;

    @Autowired
    private IncidenciasService incidenciasService;

    // Obtener todas las aulas: Accesible para cualquier usuario autenticado
    @GetMapping
    public ResponseEntity<List<AulasDTO>> getAllAulas() {
        List<AulasDTO> dtos = aulasService.findAll().stream()
                .map(AulasDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Obtener un aula por ID: Accesible para cualquier usuario autenticado
    @GetMapping("/{id}")
    public ResponseEntity<AulasDTO> getAulaById(@PathVariable Long id) {
        return aulasService.findById(id)
                .map(aula -> ResponseEntity.ok(AulasDTO.fromEntity(aula)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear aula: Solo Administrador o Técnico
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<AulasDTO> createAula(@Valid @RequestBody AulasEntity aula) {
        AulasEntity nuevaAula = aulasService.save(aula);
        return ResponseEntity.status(HttpStatus.CREATED).body(AulasDTO.fromEntity(nuevaAula));
    }

    // Actualizar aula: Solo Administrador o Técnico
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<AulasDTO> updateAula(@PathVariable Long id, @Valid @RequestBody AulasEntity aulaDetails) {
        return aulasService.findById(id)
                .map(aulaExistente -> {
                    aulaExistente.setNombre(aulaDetails.getNombre());
                    AulasEntity actualizada = aulasService.save(aulaExistente);
                    return ResponseEntity.ok(AulasDTO.fromEntity(actualizada));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar aula: Solo Administrador o Técnico
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Void> deleteAula(@PathVariable Long id) {

        if (aulasService.findById(id).isEmpty()) return ResponseEntity.notFound().build();

        // 1. Buscar todas las incidencias de este aula
        List<IncidenciasEntity> incidencias = incidenciasService.findByAulaId(id);

        // 2. Desvincularlas (poner el aula a null)
        for (IncidenciasEntity incidencia : incidencias) {
            incidencia.setAula(null);
            incidenciasService.save(incidencia);
        }
            aulasService.deleteById(id);
            return ResponseEntity.noContent().build();

    }
}