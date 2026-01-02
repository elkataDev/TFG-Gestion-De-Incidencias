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

/**
 * Controlador REST para la gestión de incidencias técnicas.
 * <p>
 * Proporciona endpoints para:
 * <ul>
 *   <li>Listar y filtrar incidencias por estado, usuario o aula</li>
 *   <li>Cambiar el estado de una incidencia (flujo: Abierto → En Progreso → Resuelto)</li>
 *   <li>Reportar nuevas incidencias</li>
 * </ul>
 * </p>
 *
 * <p>
 * Seguridad:
 * <ul>
 *   <li>GET - Accesible para usuarios autenticados</li>
 *   <li>POST - Usuarios autenticados pueden reportar incidencias</li>
 *   <li>PATCH - Solo TECNICO o ADMIN pueden cambiar estados</li>
 * </ul>
 * </p>
 *
 * <p>
 * Base URL: {@code /api/incidencias}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see IncidenciasService
 * @see IncidenciasDTO
 */
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

    /**
     * Obtiene todas las incidencias o filtra por estado.
     * <p>
     * Si no se proporciona el parámetro {@code estado}, devuelve todas las incidencias.
     * Si se proporciona, filtra solo las incidencias con ese estado.
     * </p>
     *
     * @param estado (opcional) estado de incidencia para filtrar (EN_PROGRESO, RESUELTO, etc.)
     * @return ResponseEntity con lista de IncidenciasDTO
     */
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

    /**
     * Busca todas las incidencias reportadas por un usuario específico.
     * <p>
     * Útil para que los profesores vean el historial de sus tickets reportados.
     * </p>
     *
     * @param usuarioId identificador del usuario
     * @return ResponseEntity con lista de IncidenciasDTO del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<IncidenciasDTO>> getByUsuario(@PathVariable Long usuarioId) {
        List<IncidenciasEntity> entidades = incidenciasService.findByUsuarioId(usuarioId);

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Busca todas las incidencias asociadas a un aula específica.
     * <p>
     * Permite ver el historial de problemas de un aula determinada.
     * </p>
     *
     * @param aulaId identificador del aula
     * @return ResponseEntity con lista de IncidenciasDTO del aula
     */
    @GetMapping("/aula/{aulaId}")
    public ResponseEntity<List<IncidenciasDTO>> getByAula(@PathVariable Long aulaId) {
        List<IncidenciasEntity> entidades = incidenciasService.findByAulaId(aulaId);

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Cambia el estado de una incidencia.
     * <p>
     * Flujo típico de estados: ABIERTO → EN_PROGRESO → RESUELTO
     * </p>
     * <p>
     * Cuando el estado cambia a RESUELTO, se actualiza automáticamente
     * la fecha de cierre.
     * </p>
     * <p>
     * Acceso: Solo TECNICO o ADMIN
     * </p>
     *
     * @param id identificador de la incidencia
     * @param requestBody mapa JSON con el campo "estado" y su nuevo valor
     * @return ResponseEntity con IncidenciasDTO actualizada, o mensaje de error
     */
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
            if (nuevoEstado == IncidenciasEntity.EstadoIncidencia.RESUELTO ) {
                incidencia.setFechaCierre(LocalDateTime.now());
            }

            IncidenciasEntity guardada = incidenciasService.save(incidencia);

            return ResponseEntity.ok(IncidenciasDTO.fromEntity(guardada));

        } catch (IllegalArgumentException e) {
            // 5. Capturamos si envían un estado inventado (ej: "TERMINADO")
            return ResponseEntity.badRequest().body("Estado no válido. Valores permitidos: EN_CURSO, EN_ESPERA, RESUELTO, CERRADO, REABIERTO");
        }
    }

    /**
     * Permite a un usuario reportar una nueva incidencia.
     * <p>
     * El usuario que reporta la incidencia se obtiene automáticamente del
     * contexto de seguridad (usuario autenticado).
     * </p>
     * <p>
     * El estado inicial será "ABIERTO" y la fecha de reporte se establecerá
     * automáticamente.
     * </p>
     *
     * @param dto datos de la incidencia (título, descripción, aulaId opcional)
     * @param principal objeto con la información del usuario autenticado
     * @return ResponseEntity con IncidenciasDTO de la incidencia creada
     * @throws RuntimeException si el usuario autenticado no existe o el aula no se encuentra
     */
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


        // Al guardar, el Service pondrá estado 'abierta' y fecha_reporte
        IncidenciasEntity incidenciaGuardada = incidenciasService.save(incidencia);
        return  ResponseEntity.ok(IncidenciasDTO.fromEntity(incidenciaGuardada));
    }


    @GetMapping("/filtrar")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<IncidenciasDTO>> filtrar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nombreAula
    ) {
        // Convertir Strings a enums
        IncidenciasEntity.EstadoIncidencia estadoEnum = null;
        if (estado != null) {
            try {
                estadoEnum = IncidenciasEntity.EstadoIncidencia.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Estado no válido: " + estado);
            }
        }

        IncidenciasEntity.CategoriaIncidencia categoriaEnum = null;
        if (categoria != null) {
            try {
                categoriaEnum = IncidenciasEntity.CategoriaIncidencia.valueOf(categoria.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Categoría no válida: " + categoria);
            }
        }

        List<IncidenciasEntity> entidades = incidenciasService.filtrar(estadoEnum, categoriaEnum, nombreAula);

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }}
