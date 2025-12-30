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
import java.util.List;
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
    //TODO NO DETECTA QUE EL ADMIN SEA UN ADMIN, NO SE PUEDE OBTENER LOS DATOS
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<IncidenciasDTO> updateEstado(
            @PathVariable Long id,
            @RequestBody IncidenciasEntity.EstadoIncidencia nuevoEstado) {
        // Buscamos la entidad
        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        // Modificamos el estado
        incidencia.setEstado(nuevoEstado);

        // Guardamos (el service devuelve la entidad guardada)
        IncidenciasEntity guardada = incidenciasService.save(incidencia);

        // Convertimos a DTO y devolvemos
        return ResponseEntity.ok(IncidenciasDTO.fromEntity(guardada));
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