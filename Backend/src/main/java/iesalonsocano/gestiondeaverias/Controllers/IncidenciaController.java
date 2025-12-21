package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.Services.IncidenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
@CrossOrigin(origins = "http://localhost:5173") // Permitir React (Vite)
public class IncidenciaController {

    @Autowired
    private IncidenciasService incidenciasService;

    // 1. OBTENER TODAS O FILTRAR POR ESTADO (Req 1.3.3)
    @GetMapping
    public List<IncidenciasEntity> getIncidencias(@RequestParam(required = false) IncidenciasEntity.EstadoIncidencia estado) {
        if (estado != null) {
            return incidenciasService.findByEstado(estado); // Usa tu nuevo método
        }
        return incidenciasService.findAll();
    }

    // 2. BUSCAR POR USUARIO (Para que el profesor vea sus tickets)
    @GetMapping("/usuario/{usuarioId}")
    public List<IncidenciasEntity> getByUsuario(@PathVariable Long usuarioId) {
        return incidenciasService.findByUsuarioId(usuarioId);
    }

    // 3. BUSCAR POR AULA (Para ver el historial de un aula)
    @GetMapping("/aula/{aulaId}")
    public List<IncidenciasEntity> getByAula(@PathVariable Long aulaId) {
        return incidenciasService.findByAulaId(aulaId);
    }

    // 4. CAMBIAR ESTADO (El flujo: Abierto -> En curso -> Resuelto)
    // Usamos PatchMapping porque solo modificamos un campo (el estado)
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('tecnico', 'administrador')")
    public ResponseEntity<IncidenciasEntity> updateEstado(
            @PathVariable Long id,
            @RequestBody IncidenciasEntity.EstadoIncidencia nuevoEstado) {

        // Aquí llamaríamos al método actualizarEstado que te sugerí añadir al Service
        // para que registre la fecha de cierre automáticamente
        return ResponseEntity.ok(incidenciasService.save(incidenciasService.findById(id)
                .map(i -> {
                    i.setEstado(nuevoEstado);
                    return i;
                }).orElseThrow()));
    }

    // 5. CREAR INCIDENCIA (Página de Averías)
    @PostMapping("/reportar")
    public ResponseEntity<IncidenciasEntity> reportar(@RequestBody IncidenciasEntity incidencia) {
        // Al guardar, el Service pondrá estado 'abierta' y fecha_reporte
        return ResponseEntity.ok(incidenciasService.save(incidencia));
    }
}