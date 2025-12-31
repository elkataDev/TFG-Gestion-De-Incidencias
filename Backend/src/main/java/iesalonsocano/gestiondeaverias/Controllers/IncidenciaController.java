package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.IncidenciasDTO;
import iesalonsocano.gestiondeaverias.Services.AulasService;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;
import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.Services.IncidenciasService;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incidencias")
@CrossOrigin(origins = "http://localhost:5173") // Permitir React (Vite)
public class IncidenciaController {

    @Autowired
    private IncidenciasService incidenciasService;
    @Autowired
    private UsuariosService usuariosService;
    @Autowired
    private AulasService aulasService;

    // 1. OBTENER TODAS O FILTRAR POR ESTADO (Req 1.3.3)
    @GetMapping
    public ResponseEntity<List<IncidenciasDTO>> getIncidencias(@RequestParam(required = false) IncidenciasEntity.EstadoIncidencia estado) {
        List<IncidenciasEntity> entidades;

        if (estado != null) {
            entidades = incidenciasService.findByEstado(estado);
        } else {
            entidades = incidenciasService.findAll();
        }

        // Convertimos la lista de Entidades a lista de DTOs
        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // 2. BUSCAR POR USUARIO (Para que el profesor vea sus tickets)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<IncidenciasDTO>> getByUsuario(@PathVariable Long usuarioId) {
        List<IncidenciasEntity> entidades = incidenciasService.findByUsuarioId(usuarioId);

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // 3. BUSCAR POR AULA (Para ver el historial de un aula)
    @GetMapping("/aula/{aulaId}")
    public ResponseEntity<List<IncidenciasDTO>> getByAula(@PathVariable Long aulaId) {
        List<IncidenciasEntity> entidades = incidenciasService.findByAulaId(aulaId);

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // 4. CAMBIAR ESTADO (El flujo: Abierto -> En curso -> Resuelto)
    // Usamos PatchMapping porque solo modificamos un campo (el estado)
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<?> updateEstado(
            // Usa ? para poder devolver errores de texto si falla
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) { // 1. Recibimos un JSON genérico

        // 2. Extraemos el valor del campo "estado"
        String nuevoEstadoStr = requestBody.get("estado");

        if (nuevoEstadoStr == null) {
            return ResponseEntity.badRequest().body("El campo 'estado' es obligatorio");
        }

        try {
            // 3. Convertimos el String al Enum (Valida que sea uno correcto: EN_CURSO, RESUELTO...)
            IncidenciasEntity.EstadoIncidencia nuevoEstado =
                    IncidenciasEntity.EstadoIncidencia.valueOf(nuevoEstadoStr.toUpperCase());

            // 4. Lógica de negocio
            IncidenciasEntity incidencia = incidenciasService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

            incidencia.setEstado(nuevoEstado);

            // Actualizamos fecha de cierre si corresponde
            if (nuevoEstado == IncidenciasEntity.EstadoIncidencia.RESUELTO ||
                    nuevoEstado == IncidenciasEntity.EstadoIncidencia.CERRADO) {
                incidencia.setFechaCierre(LocalDateTime.now());
            }

            IncidenciasEntity guardada = incidenciasService.save(incidencia);

            return ResponseEntity.ok(IncidenciasDTO.fromEntity(guardada));

        } catch (IllegalArgumentException e) {
            // 5. Capturamos si envían un estado inventado (ej: "TERMINADO")
            return ResponseEntity.badRequest().body("Estado no válido. Valores permitidos: EN_CURSO, EN_ESPERA, RESUELTO, CERRADO, REABIERTO");
        }
    }

    // 5. CREAR INCIDENCIA (Página de Averías)
    @PostMapping("/reportar")
    public ResponseEntity<IncidenciasDTO> reportar(@RequestBody IncidenciasDTO dto, Principal principal) {
        // 1. Buscamos al usuario real que está logueado
        String username = principal.getName();
        UsuariosEntity usuario = usuariosService.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Mapeamos a la entidad
        IncidenciasEntity incidencia = new IncidenciasEntity();
        incidencia.setTitulo(dto.getTitulo());
        incidencia.setDescripcion(dto.getDescripcion());

        // ASIGNAMOS EL USUARIO AUTENTICADO
        incidencia.setUsuario(usuario);

        // 3. Asignamos el aula si viene en el DTO
        if (dto.getAulaId() != null) {
            AulasEntity aula = aulasService.findById(dto.getAulaId())
                    .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
            incidencia.setAula(aula);
        }
        // Al guardar, el Service pondrá estado 'abierta' y fecha_reporte
        IncidenciasEntity incidenciaGuardada = incidenciasService.save(incidencia);
        return  ResponseEntity.ok(IncidenciasDTO.fromEntity(incidenciaGuardada));
    }
}