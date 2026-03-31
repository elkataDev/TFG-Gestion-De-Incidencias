package iesalonsocano.gestiondeaverias.Services.impl;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.Repository.UsuariosRepository;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.Repository.UsuariosRepository;
import iesalonsocano.gestiondeaverias.Services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariosServiceImpl(
            UsuariosRepository usuariosRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }



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

        usuario.setPassword(
                passwordEncoder.encode(usuario.getPassword())
        );

        if (usuario.getRol() == null) {
            usuario.setRol(UsuariosEntity.RolUsuario.USUARIO);
        }

        usuario.setActivo(true);

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