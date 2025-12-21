<<<<<<< Updated upstream:Backend/src/main/java/iesalonsocano/gestiondeaverias/Services/UsuariosServiceImpl.java
package iesalonsocano.gestiondeaverias.service.impl; // Usando el subpaquete 'impl'

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.repository.UsuariosRepository;
import iesalonsocano.gestiondeaverias.service.UsuariosService;
=======
package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.Repository.UsuariosRepository;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;
>>>>>>> Stashed changes:Backend/src/main/java/iesalonsocano/gestiondeaverias/Services/impl/UsuariosServiceImpl.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de UsuariosService.
 * ATENCIÓN: Esta versión NO hashea contraseñas. Es para desarrollo/pruebas.
 * Implementation of UsuariosService.
 * ATTENTION!: This version DOES NOT hash passwords. It is for development/testing only.
 */
@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public List<UsuariosEntity> findAll() {
        return usuariosRepository.findAll();
    }

    @Override
    public Optional<UsuariosEntity> findById(Long id) {
        return usuariosRepository.findById(id);
    }

    @Override
    public UsuariosEntity save(UsuariosEntity usuario) {

        // --- LÓGICA DE GUARDADO SIMPLE ---
        // La contraseña se guarda en texto plano tal como viene.
        // Simple save logic. Password is saved as plain text as received.

        return usuariosRepository.save(usuario);
    }

    @Override
    public void deleteById(Long id) {
        // Elimina el usuario. / Deletes the user.
        usuariosRepository.deleteById(id);
    }

    // --- Métodos de búsqueda del repositorio ---
    // --- Repository search methods ---

    @Override
    public Optional<UsuariosEntity> findByNombreUsuario(String nombreUsuario) {
        return usuariosRepository.findByNombreUsuario(nombreUsuario);
    }

    @Override
    public Optional<UsuariosEntity> findByEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }

    @Override
    public List<UsuariosEntity> findByActivoTrue() {
        return usuariosRepository.findByActivoTrue();
    }
}