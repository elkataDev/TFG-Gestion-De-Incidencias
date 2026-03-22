package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.UsuariosDTO;
import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de usuarios del sistema.
 * <p>
 * Proporciona endpoints para administrar usuarios (profesores, técnicos, administradores)
 * con operaciones CRUD completas.
 * </p>
 *
 * <p>
 * Seguridad:
 * <ul>
 *   <li>Todos los endpoints requieren rol ADMIN o TECNICO</li>
 *   <li>Las contraseñas se encriptan automáticamente con BCrypt</li>
 * </ul>
 * </p>
 *
 * <p>
 * Base URL: {@code /api/usuarios}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see UsuariosService
 * @see UsuariosDTO
 */
@RestController
@RequestMapping("api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    /**
     * Obtiene la lista de todos los usuarios del sistema.
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @return ResponseEntity con lista de UsuariosDTO (sin contraseñas)
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<List<UsuariosDTO>> getAllUsuarios() {
        List<UsuariosDTO> dtos = usuariosService.findAll().stream()
                .map(UsuariosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene un usuario específico por su ID.
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param id identificador del usuario
     * @return ResponseEntity con UsuariosDTO si existe, o 404 Not Found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<UsuariosDTO> getUsuarioById(@PathVariable Long id) {
        return usuariosService.findById(id)
                .map(u -> ResponseEntity.ok(UsuariosDTO.fromEntity(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * <p>
     * La contraseña se cifra automáticamente con BCrypt.
     * </p>
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param usuario entidad con los datos del usuario a crear
     * @return ResponseEntity con UsuariosDTO creado y código HTTP 201 Created
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<UsuariosDTO> createUsuario(@Valid @RequestBody UsuariosEntity usuario) {
        // El service debería gestionar el cifrado de password si usas BCrypt
        UsuariosEntity nuevo = usuariosService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuariosDTO.fromEntity(nuevo));
    }

    /**
     * Actualiza los datos de un usuario existente.
     * <p>
     * Si se proporciona una nueva contraseña, se cifra automáticamente.
     * Si el campo password está vacío, se mantiene la contraseña actual.
     * </p>
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param id identificador del usuario a actualizar
     * @param details entidad con los nuevos datos del usuario
     * @return ResponseEntity con UsuariosDTO actualizado o 404 Not Found
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<UsuariosDTO> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuariosEntity details) {
        return usuariosService.findById(id)
                .map(u -> {
                    // Mapeo directo de campos de UsuariosEntity
                    u.setNombreUsuario(details.getNombreUsuario());
                    u.setEmail(details.getEmail());
                    u.setRol(details.getRol());
                    u.setActivo(details.getActivo());

                    // Solo actualizamos password si se envía uno nuevo
                    if (details.getPassword() != null && !details.getPassword().isBlank()) {
                        u.setPassword(details.getPassword());
                    }

                    UsuariosEntity actualizado = usuariosService.save(u);
                    return ResponseEntity.ok(UsuariosDTO.fromEntity(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un usuario del sistema.
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param id identificador del usuario a eliminar
     * @return ResponseEntity vacío con código 204 No Content o 404 Not Found
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (usuariosService.findById(id).isPresent()) {
            usuariosService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}