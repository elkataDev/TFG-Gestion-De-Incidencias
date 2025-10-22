package iesalonsocano.gestiondeaverias.controller;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    // Obtener todos los usuarios
    @GetMapping
    public List<UsuariosEntity> getAllUsuarios() {
        return usuariosService.findAll();
    }

    // Obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuariosEntity> getUsuarioById(@PathVariable Long id) {
        Optional<UsuariosEntity> optionalUsuario = usuariosService.findById(id);

        if (optionalUsuario.isPresent()) {
            return ResponseEntity.ok(optionalUsuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuariosEntity> createUsuario(@Valid @RequestBody UsuariosEntity usuario) {
        UsuariosEntity nuevoUsuario = usuariosService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuariosEntity> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuariosEntity usuarioDetails) {
        Optional<UsuariosEntity> optionalUsuario = usuariosService.findById(id);

        if (optionalUsuario.isPresent()) {
            UsuariosEntity usuarioExistente = optionalUsuario.get();

            usuarioExistente.setNombreUsuario(usuarioDetails.getNombreUsuario());
            usuarioExistente.setEmail(usuarioDetails.getEmail());
            usuarioExistente.setPassword(usuarioDetails.getPassword());
            usuarioExistente.setActivo(usuarioDetails.getActivo());

            UsuariosEntity actualizado = usuariosService.save(usuarioExistente);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<UsuariosEntity> optionalUsuario = usuariosService.findById(id);

        if (optionalUsuario.isPresent()) {
            usuariosService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}