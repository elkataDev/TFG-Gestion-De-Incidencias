package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.ParteTrabajoDTO;
import iesalonsocano.gestiondeaverias.Services.IncidenciasService;
import iesalonsocano.gestiondeaverias.Services.ParteTrabajoService;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de Partes de Trabajo.
 * <p>
 * Permite a los técnicos registrar el tiempo invertido y materiales utilizados
 * en cada ticket de incidencia. Solo accesible para TECNICO y ADMIN.
 * </p>
 *
 * <p>
 * Base URL: {@code /api/incidencias/{incidenciaId}/partes}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/incidencias/{incidenciaId}/partes")
public class ParteTrabajoController {

    @Autowired
    private ParteTrabajoService parteTrabajoService;

    @Autowired
    private IncidenciasService incidenciasService;

    @Autowired
    private UsuariosService usuariosService;

    /**
     * Obtiene todos los partes de trabajo de una incidencia.
     * Acceso: TECNICO y ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<List<ParteTrabajoDTO>> getPartes(@PathVariable Long incidenciaId) {
        List<ParteTrabajoEntity> partes = parteTrabajoService.findByIncidenciaId(incidenciaId);
        List<ParteTrabajoDTO> dtos = partes.stream()
                .map(ParteTrabajoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene el total de horas invertidas en una incidencia.
     * Acceso: TECNICO y ADMIN.
     */
    @GetMapping("/total-horas")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getTotalHoras(@PathVariable Long incidenciaId) {
        BigDecimal totalHoras = parteTrabajoService.calcularTotalHoras(incidenciaId);
        return ResponseEntity.ok(Map.of(
                "incidenciaId", incidenciaId,
                "totalHoras", totalHoras
        ));
    }

    /**
     * Registra un nuevo parte de trabajo para una incidencia.
     * El técnico se obtiene automáticamente del contexto de seguridad.
     * Acceso: TECNICO y ADMIN.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<?> addParte(
            @PathVariable Long incidenciaId,
            @RequestBody Map<String, Object> body,
            Principal principal) {

        IncidenciasEntity incidencia = incidenciasService.findById(incidenciaId)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        UsuariosEntity tecnico = usuariosService.findByNombreUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Extraer campos del body
        Object tiempoObj = body.get("tiempoHoras");
        if (tiempoObj == null) {
            return ResponseEntity.badRequest().body("El campo 'tiempoHoras' es obligatorio");
        }

        BigDecimal tiempoHoras;
        try {
            tiempoHoras = new BigDecimal(tiempoObj.toString());
            if (tiempoHoras.compareTo(BigDecimal.valueOf(0.1)) < 0) {
                return ResponseEntity.badRequest().body("El tiempo mínimo es 0.1 horas");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("El campo 'tiempoHoras' debe ser un número válido");
        }

        String descripcionTrabajo = body.get("descripcionTrabajo") != null
                ? body.get("descripcionTrabajo").toString() : null;
        String piezasUtilizadas = body.get("piezasUtilizadas") != null
                ? body.get("piezasUtilizadas").toString() : null;

        ParteTrabajoEntity parte = ParteTrabajoEntity.builder()
                .incidencia(incidencia)
                .tecnico(tecnico)
                .tiempoHoras(tiempoHoras)
                .descripcionTrabajo(descripcionTrabajo)
                .piezasUtilizadas(piezasUtilizadas)
                .build();

        ParteTrabajoEntity saved = parteTrabajoService.save(parte);
        return ResponseEntity.status(HttpStatus.CREATED).body(ParteTrabajoDTO.fromEntity(saved));
    }

    /**
     * Elimina un parte de trabajo específico.
     * Acceso: solo ADMIN.
     */
    @DeleteMapping("/{parteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteParte(
            @PathVariable Long incidenciaId,
            @PathVariable Long parteId) {
        return parteTrabajoService.findById(parteId)
                .map(p -> {
                    parteTrabajoService.deleteById(parteId);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
