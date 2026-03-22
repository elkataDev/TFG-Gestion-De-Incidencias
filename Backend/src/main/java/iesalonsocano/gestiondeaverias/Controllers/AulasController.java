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

/**
 * Controlador REST para la gestión de aulas del centro educativo.
 * <p>
 * Proporciona endpoints para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre las aulas del instituto.
 * </p>
 *
 * <p>
 * Seguridad:
 * <ul>
 *   <li>GET - Accesible para cualquier usuario autenticado</li>
 *   <li>POST, PUT, DELETE - Solo ADMIN o TECNICO</li>
 * </ul>
 * </p>
 *
 * <p>
 * Base URL: {@code /api/aulas}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see AulasService
 * @see AulasDTO
 */
@RestController
@RequestMapping("api/aulas")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost"})
public class AulasController {

    @Autowired
    private AulasService aulasService;

    @Autowired
    private IncidenciasService incidenciasService;

    /**
     * Obtiene la lista de todas las aulas del centro.
     * <p>
     * Acceso: Cualquier usuario autenticado.
     * </p>
     *
     * @return ResponseEntity con lista de AulasDTO y código HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<AulasDTO>> getAllAulas() {
        List<AulasDTO> dtos = aulasService.findAll().stream()
                .map(AulasDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene un aula específica por su identificador.
     * <p>
     * Acceso: Cualquier usuario autenticado.
     * </p>
     *
     * @param id identificador único del aula
     * @return ResponseEntity con AulasDTO si existe, o 404 Not Found si no se encuentra
     */
    @GetMapping("/{id}")
    public ResponseEntity<AulasDTO> getAulaById(@PathVariable Long id) {
        return aulasService.findById(id)
                .map(aula -> ResponseEntity.ok(AulasDTO.fromEntity(aula)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un aula nueva en el sistema.
     * <p>
     * Acceso: Solo ADMIN o TECNICO.
     * </p>
     *
     * @param aula entidad con los datos del aula a crear
     * @return ResponseEntity con AulasDTO del aula creada y código HTTP 201 Created
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<AulasDTO> createAula(@Valid @RequestBody AulasEntity aula) {
        AulasEntity nuevaAula = aulasService.save(aula);
        return ResponseEntity.status(HttpStatus.CREATED).body(AulasDTO.fromEntity(nuevaAula));
    }

    /**
     * Actualiza los datos de un aula existente.
     * <p>
     * Acceso: Solo ADMIN o TECNICO.
     * </p>
     *
     * @param id identificador del aula a actualizar
     * @param aulaDetails entidad con los nuevos datos del aula
     * @return ResponseEntity con AulasDTO actualizada o 404 Not Found si no existe
     */
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

    /**
     * Elimina un aula del sistema.
     * <p>
     * Acceso: Solo ADMIN o TECNICO.
     * </p>
     * <p>
     * Antes de eliminar el aula, desvincula todas las incidencias asociadas
     * estableciendo su referencia a aula como null.
     * </p>
     *
     * @param id identificador del aula a eliminar
     * @return ResponseEntity vacío con código 204 No Content, o 404 Not Found si no existe
     */
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