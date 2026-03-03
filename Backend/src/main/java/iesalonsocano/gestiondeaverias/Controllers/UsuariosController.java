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

@RestController
@RequestMapping("api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    // Obtener todos los usuarios: Solo Administrador o Técnico TIC
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<List<UsuariosDTO>> getAllUsuarios() {
        List<UsuariosDTO> dtos = usuariosService.findAll().stream()
                .map(UsuariosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Obtener un usuario por ID: Solo Administrador o Técnico TIC
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<UsuariosDTO> getUsuarioById(@PathVariable Long id) {
        return usuariosService.findById(id)
                .map(u -> ResponseEntity.ok(UsuariosDTO.fromEntity(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo usuario: Solo Administrador o Técnico TIC
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<UsuariosDTO> createUsuario(@Valid @RequestBody UsuariosEntity usuario) {
        // El service debería gestionar el cifrado de password si usas BCrypt
        UsuariosEntity nuevo = usuariosService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuariosDTO.fromEntity(nuevo));
    }

    // Actualizar usuario: Solo Administrador o Técnico TIC
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

    // Eliminar usuario: Solo Administrador o Técnico TIC
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