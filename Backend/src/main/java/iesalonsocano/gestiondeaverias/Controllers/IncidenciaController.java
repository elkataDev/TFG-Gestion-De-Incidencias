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
public class IncidenciaController {

    @Autowired
    private IncidenciasService incidenciasService;
    @Autowired
    private UsuariosService usuariosService;
    @Autowired
    private AulasService aulasService;
    @Autowired
    private iesalonsocano.gestiondeaverias.Services.StorageService storageService;
    @Autowired
    private iesalonsocano.gestiondeaverias.Services.ComentarioService comentarioService;
    @Autowired
    private iesalonsocano.gestiondeaverias.Services.HistorialEstadoService historialEstadoService;

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

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciasDTO> getIncidenciaById(@PathVariable Long id) {
        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        return ResponseEntity.ok(IncidenciasDTO.fromEntity(incidencia));
    }

    @GetMapping("/mis-incidencias")
    public ResponseEntity<List<IncidenciasDTO>> getMisIncidencias(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        UsuariosEntity usuario = usuariosService.findByNombreUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<IncidenciasEntity> entidades = incidenciasService.findByUsuarioId(usuario.getId());
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
            @RequestBody Map<String, String> requestBody,
            Principal principal) { // 1. Recibimos un JSON genérico

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

            IncidenciasEntity.EstadoIncidencia estadoAnterior = incidencia.getEstado();

            incidencia.setEstado(nuevoEstado);

            // Actualizamos fecha de cierre si corresponde
            if (nuevoEstado == IncidenciasEntity.EstadoIncidencia.RESUELTO ||
                    nuevoEstado == IncidenciasEntity.EstadoIncidencia.CERRADO) {
                incidencia.setFechaCierre(LocalDateTime.now());
            }

            IncidenciasEntity guardada = incidenciasService.save(incidencia);

            if (estadoAnterior != nuevoEstado) {
                UsuariosEntity usuarioLogueado = null;
                if (principal != null) {
                    usuarioLogueado = usuariosService.findByNombreUsuario(principal.getName()).orElse(null);
                }
                historialEstadoService.registrarCambio(guardada, estadoAnterior, nuevoEstado, usuarioLogueado);
            }

            return ResponseEntity.ok(IncidenciasDTO.fromEntity(guardada));

        } catch (IllegalArgumentException e) {
            // 5. Capturamos si envían un estado inventado (ej: "TERMINADO")
            return ResponseEntity.badRequest().body("Estado no válido. Valores permitidos: ABIERTO, EN_PROGRESO, RESUELTO, CERRADO");
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
    @PostMapping(value = "/reportar", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> reportar(
            @RequestPart("incidencia") IncidenciasDTO dto,
            @RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file,
            Principal principal) {
        // 1. Buscamos al usuario real que está logueado
        String username = principal.getName();
        UsuariosEntity usuario = usuariosService.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Mapeamos a la entidad
        IncidenciasEntity incidencia = new IncidenciasEntity();
        incidencia.setTitulo(dto.getTitulo());
        incidencia.setDescripcion(dto.getDescripcion());
        
        if (dto.getCategoria() != null) {
            try {
                incidencia.setCategoria(IncidenciasEntity.CategoriaIncidencia.valueOf(dto.getCategoria().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Categoría no válida. Valores: HARDWARE, SOFTWARE, RED");
            }
        }

        // ASIGNAMOS EL USUARIO AUTENTICADO
        incidencia.setUsuario(usuario);

        // ASIGNAMOS EL AULA SI VIENE EN EL DTO
        if (dto.getAulaId() != null) {
            AulasEntity aula = aulasService.findById(dto.getAulaId())
                    .orElseThrow(() -> new RuntimeException("Aula no encontrada con id: " + dto.getAulaId()));
            incidencia.setAula(aula);
        }

        // File upload
        if (file != null && !file.isEmpty()) {
            String filename = storageService.store(file);
            incidencia.setAdjuntoUrl(filename);
        }

        // Al guardar, el Service pondrá estado 'abierta' y fecha_reporte
        IncidenciasEntity incidenciaGuardada = incidenciasService.save(incidencia);
        return  ResponseEntity.ok(IncidenciasDTO.fromEntity(incidenciaGuardada));
    }

    @GetMapping("/archivos/{filename:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> serveFile(@PathVariable String filename) {
        org.springframework.core.io.Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(
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
                return ResponseEntity.badRequest().body("Estado no válido: " + estado + ". Valores permitidos: ABIERTO, EN_PROGRESO, RESUELTO, CERRADO");
            }
        }

        IncidenciasEntity.CategoriaIncidencia categoriaEnum = null;
        if (categoria != null) {
            try {
                categoriaEnum = IncidenciasEntity.CategoriaIncidencia.valueOf(categoria.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Categoría no válida: " + categoria + ". Valores permitidos: HARDWARE, SOFTWARE, RED");
            }
        }

        List<IncidenciasEntity> entidades = incidenciasService.filtrar(estadoEnum, categoriaEnum, nombreAula);

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<iesalonsocano.gestiondeaverias.DTO.ComentarioDTO>> getComentarios(@PathVariable Long id) {
        List<iesalonsocano.gestiondeaverias.entity.ComentarioEntity> comentarios = comentarioService.findByIncidenciaId(id);
        List<iesalonsocano.gestiondeaverias.DTO.ComentarioDTO> dtos = comentarios.stream()
                .map(iesalonsocano.gestiondeaverias.DTO.ComentarioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<iesalonsocano.gestiondeaverias.DTO.ComentarioDTO> addComentario(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        String texto = requestBody.get("texto");
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        UsuariosEntity usuario = usuariosService.findByNombreUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        iesalonsocano.gestiondeaverias.entity.ComentarioEntity comentario = new iesalonsocano.gestiondeaverias.entity.ComentarioEntity();
        comentario.setTexto(texto);
        comentario.setIncidencia(incidencia);
        comentario.setUsuario(usuario);

        iesalonsocano.gestiondeaverias.entity.ComentarioEntity saved = comentarioService.save(comentario);
        return ResponseEntity.ok(iesalonsocano.gestiondeaverias.DTO.ComentarioDTO.fromEntity(saved));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<iesalonsocano.gestiondeaverias.DTO.HistorialEstadoDTO>> getHistorial(@PathVariable Long id) {
        List<iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity> historial = historialEstadoService.findByIncidenciaId(id);
        List<iesalonsocano.gestiondeaverias.DTO.HistorialEstadoDTO> dtos = historial.stream()
                .map(iesalonsocano.gestiondeaverias.DTO.HistorialEstadoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
