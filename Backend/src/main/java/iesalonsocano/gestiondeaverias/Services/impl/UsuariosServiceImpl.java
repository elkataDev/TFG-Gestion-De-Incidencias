package iesalonsocano.gestiondeaverias.services.impl;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import iesalonsocano.gestiondeaverias.repository.UsuariosRepository;
import iesalonsocano.gestiondeaverias.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UsuariosEntity> findAll() {
        return usuariosRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuariosEntity> findById(Long id) {
        return usuariosRepository.findById(id);
    }

    @Override
    @Transactional
    public UsuariosEntity save(UsuariosEntity usuario) {
        return usuariosRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        usuariosRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuariosEntity> findByNombreUsuario(String nombreUsuario) {
        return usuariosRepository.findByNombreUsuario(nombreUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuariosEntity> findByEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuariosEntity> findByActivoTrue() {
        return usuariosRepository.findByActivoTrue();
    }
}