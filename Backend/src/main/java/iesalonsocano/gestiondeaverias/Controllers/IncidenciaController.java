package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.IncidenciasDTO;
import iesalonsocano.gestiondeaverias.DTO.ParteTrabajoDTO;
import iesalonsocano.gestiondeaverias.Repository.ParteTrabajoRepository;
import iesalonsocano.gestiondeaverias.Services.AulasService;
import iesalonsocano.gestiondeaverias.Services.InventarioService;
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
import java.math.BigDecimal;
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
    private InventarioService inventarioService;
    @Autowired
    private iesalonsocano.gestiondeaverias.Services.StorageService storageService;
    @Autowired
    private iesalonsocano.gestiondeaverias.Services.ComentarioService comentarioService;
    @Autowired
    private iesalonsocano.gestiondeaverias.Services.HistorialEstadoService historialEstadoService;
    @Autowired
    private ParteTrabajoRepository parteTrabajoRepository;

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
    public ResponseEntity<List<IncidenciasDTO>> getIncidencias(
            @RequestParam(required = false) IncidenciasEntity.EstadoIncidencia estado,
            Principal principal) {
        UsuariosEntity usuarioActual = getUsuarioActual(principal);
        List<IncidenciasEntity> entidades;

        if (!isAdminOrTech(usuarioActual)) {
            entidades = incidenciasService.findByUsuarioId(usuarioActual.getId());
            if (estado != null) {
                entidades = entidades.stream()
                        .filter(i -> i.getEstado() == estado)
                        .collect(Collectors.toList());
            }
        } else if (estado != null) {
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
    public ResponseEntity<List<IncidenciasDTO>> getByUsuario(@PathVariable Long usuarioId, Principal principal) {
        UsuariosEntity usuarioActual = getUsuarioActual(principal);
        if (!isAdminOrTech(usuarioActual) && !usuarioActual.getId().equals(usuarioId)) {
            return ResponseEntity.status(403).build();
        }

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
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<List<IncidenciasDTO>> getByAula(@PathVariable Long aulaId) {
        List<IncidenciasEntity> entidades = incidenciasService.findByAulaId(aulaId);

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciasDTO> getIncidenciaById(@PathVariable Long id, Principal principal) {
        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        if (!canAccessIncidencia(incidencia, principal)) {
            return ResponseEntity.status(403).build();
        }
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
            return ResponseEntity.badRequest().body("Estado no válido. Valores permitidos: ABIERTO, EN_PROGRESO, EN_ESPERA, RESUELTO, CERRADO, REABIERTO");
        }
    }

    @PatchMapping("/{id}/asignar-tecnico")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<?> asignarTecnico(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody
    ) {
        Object tecnicoIdRaw = requestBody.get("tecnicoId");
        if (tecnicoIdRaw == null) {
            return ResponseEntity.badRequest().body("El campo 'tecnicoId' es obligatorio");
        }

        Long tecnicoId;
        try {
            tecnicoId = Long.valueOf(String.valueOf(tecnicoIdRaw));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("tecnicoId no válido");
        }

        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        UsuariosEntity tecnico = usuariosService.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        if (tecnico.getRol() != UsuariosEntity.RolUsuario.TECNICO && tecnico.getRol() != UsuariosEntity.RolUsuario.ADMIN) {
            return ResponseEntity.badRequest().body("Solo se puede asignar un usuario con rol TECNICO o ADMIN");
        }

        incidencia.setTecnicoAsignado(tecnico);
        IncidenciasEntity guardada = incidenciasService.save(incidencia);
        return ResponseEntity.ok(IncidenciasDTO.fromEntity(guardada));
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
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Usuario no autenticado"));
        }

        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El título es obligatorio"));
        }

        if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "La descripción es obligatoria"));
        }

        if (dto.getCategoria() == null || dto.getCategoria().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "La categoría es obligatoria"));
        }

        if (dto.getActivoId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "El activo afectado es obligatorio"));
        }

        // 1. Buscamos al usuario real que está logueado
        String username = principal.getName();
        UsuariosEntity usuario = usuariosService.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Mapeamos a la entidad
        IncidenciasEntity incidencia = new IncidenciasEntity();
        incidencia.setTitulo(dto.getTitulo().trim());
        incidencia.setDescripcion(dto.getDescripcion().trim());
        
        try {
            incidencia.setCategoria(IncidenciasEntity.CategoriaIncidencia.valueOf(dto.getCategoria().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Categoría no válida. Valores: HARDWARE, SOFTWARE, RED"));
        }

        // ASIGNAMOS EL USUARIO AUTENTICADO
        incidencia.setUsuario(usuario);

        var activo = inventarioService.findById(dto.getActivoId())
                .orElseThrow(() -> new RuntimeException("Activo no encontrado con id: " + dto.getActivoId()));
        incidencia.setActivo(activo);
        incidencia.setAula(activo.getAula());

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
    public ResponseEntity<?> serveFile(@PathVariable String filename) {
        try {
            org.springframework.core.io.Resource file = storageService.loadAsResource(filename);
            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Archivo no encontrado o ya no existe en el servidor.");
        }
    }


    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nombreAula,
            Principal principal
    ) {
        UsuariosEntity usuarioActual = getUsuarioActual(principal);
        // Convertir Strings a enums
        IncidenciasEntity.EstadoIncidencia estadoEnum = null;
        if (estado != null) {
            try {
                estadoEnum = IncidenciasEntity.EstadoIncidencia.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Estado no válido: " + estado + ". Valores permitidos: ABIERTO, EN_PROGRESO, EN_ESPERA, RESUELTO, CERRADO, REABIERTO");
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
        if (!isAdminOrTech(usuarioActual)) {
            entidades = entidades.stream()
                    .filter(i -> i.getUsuario() != null && usuarioActual.getId().equals(i.getUsuario().getId()))
                    .collect(Collectors.toList());
        }

        List<IncidenciasDTO> dtos = entidades.stream()
                .map(IncidenciasDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<iesalonsocano.gestiondeaverias.DTO.ComentarioDTO>> getComentarios(@PathVariable Long id, Principal principal) {
        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        if (!canAccessIncidencia(incidencia, principal)) {
            return ResponseEntity.status(403).build();
        }

        String role = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", ""))
                .findFirst().orElse("USUARIO");
        boolean isAdminOrTech = "ADMIN".equals(role) || "TECNICO".equals(role);

        List<iesalonsocano.gestiondeaverias.entity.ComentarioEntity> comentarios = comentarioService.findByIncidenciaId(id);
        if (!isAdminOrTech) {
            comentarios = comentarios.stream().filter(c -> !Boolean.TRUE.equals(c.getEsInterna())).collect(Collectors.toList());
        }
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
        if (!canAccessIncidencia(incidencia, principal)) {
            return ResponseEntity.status(403).build();
        }

        UsuariosEntity usuario = usuariosService.findByNombreUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        iesalonsocano.gestiondeaverias.entity.ComentarioEntity comentario = new iesalonsocano.gestiondeaverias.entity.ComentarioEntity();
        comentario.setTexto(texto);
        boolean esInterna = Boolean.parseBoolean(String.valueOf(requestBody.getOrDefault("esInterna", "false")));
        if (esInterna) {
            String role = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .filter(a -> a.startsWith("ROLE_"))
                    .map(a -> a.replace("ROLE_", ""))
                    .findFirst().orElse("USUARIO");
            if (!"ADMIN".equals(role) && !"TECNICO".equals(role)) {
                return ResponseEntity.status(403).build();
            }
        }
        comentario.setEsInterna(esInterna);
        comentario.setIncidencia(incidencia);
        comentario.setUsuario(usuario);

        iesalonsocano.gestiondeaverias.entity.ComentarioEntity saved = comentarioService.save(comentario);
        return ResponseEntity.ok(iesalonsocano.gestiondeaverias.DTO.ComentarioDTO.fromEntity(saved));
    }

    @GetMapping("/{id}/partes")
    public ResponseEntity<List<ParteTrabajoDTO>> getPartes(@PathVariable Long id, Principal principal) {
        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        if (!canAccessIncidencia(incidencia, principal)) {
            return ResponseEntity.status(403).build();
        }

        List<iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity> partes = parteTrabajoRepository.findByIncidenciaId(id);
        return ResponseEntity.ok(partes.stream().map(ParteTrabajoDTO::fromEntity).collect(Collectors.toList()));
    }

    @PostMapping("/{id}/partes")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<?> addParte(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody,
            Principal principal
    ) {
        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        UsuariosEntity tecnico = usuariosService.findByNombreUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Integer minutos;
        try {
            minutos = Integer.valueOf(String.valueOf(requestBody.getOrDefault("minutos", "0")));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("minutos debe ser un número");
        }

        String descripcion = String.valueOf(requestBody.getOrDefault("descripcion", "")).trim();
        if (minutos <= 0 || descripcion.isEmpty()) {
            return ResponseEntity.badRequest().body("minutos y descripcion son obligatorios");
        }

        BigDecimal coste = null;
        Object costeRaw = requestBody.get("coste");
        if (costeRaw != null && !String.valueOf(costeRaw).isBlank()) {
            try {
                coste = new BigDecimal(String.valueOf(costeRaw));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("coste no válido");
            }
        }

        iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity parte = iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity.builder()
                .incidencia(incidencia)
                .tecnico(tecnico)
                .minutos(minutos)
                .descripcion(descripcion)
                .piezasUsadas(String.valueOf(requestBody.getOrDefault("piezasUsadas", "")))
                .coste(coste)
                .build();

        iesalonsocano.gestiondeaverias.entity.ParteTrabajoEntity saved = parteTrabajoRepository.save(parte);
        return ResponseEntity.ok(ParteTrabajoDTO.fromEntity(saved));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<iesalonsocano.gestiondeaverias.DTO.HistorialEstadoDTO>> getHistorial(@PathVariable Long id, Principal principal) {
        IncidenciasEntity incidencia = incidenciasService.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        if (!canAccessIncidencia(incidencia, principal)) {
            return ResponseEntity.status(403).build();
        }

        List<iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity> historial = historialEstadoService.findByIncidenciaId(id);
        List<iesalonsocano.gestiondeaverias.DTO.HistorialEstadoDTO> dtos = historial.stream()
                .map(iesalonsocano.gestiondeaverias.DTO.HistorialEstadoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<Void> deleteIncidencia(@PathVariable Long id) {
        if (incidenciasService.findById(id).isPresent()) {
            incidenciasService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private UsuariosEntity getUsuarioActual(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return usuariosService.findByNombreUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private boolean isAdminOrTech(UsuariosEntity usuario) {
        return usuario.getRol() == UsuariosEntity.RolUsuario.ADMIN
                || usuario.getRol() == UsuariosEntity.RolUsuario.TECNICO;
    }

    private boolean canAccessIncidencia(IncidenciasEntity incidencia, Principal principal) {
        UsuariosEntity usuarioActual = getUsuarioActual(principal);
        if (isAdminOrTech(usuarioActual)) {
            return true;
        }
        return incidencia.getUsuario() != null
                && incidencia.getUsuario().getId() != null
                && incidencia.getUsuario().getId().equals(usuarioActual.getId());
    }
}
