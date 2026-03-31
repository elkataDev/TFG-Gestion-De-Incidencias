package iesalonsocano.gestiondeaverias.Services.impl;

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
 * <p>
 * Se encarga de la gestión de usuarios, incluyendo el cifrado de contraseñas
 * antes de la persistencia.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 */
@Service
public class UsuariosServiceImpl implements UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
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
        // Encriptar contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Valores por defecto si no vienen informados
        if (usuario.getRol() == null) {
            usuario.setRol(UsuariosEntity.RolUsuario.USUARIO);
        }
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        return usuariosRepository.save(usuario);
    }
    @Override
    public void deleteById(Long id) {
        usuariosRepository.deleteById(id);
    }

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
