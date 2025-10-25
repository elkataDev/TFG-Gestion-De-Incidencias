package iesalonsocano.gestiondeaverias.repository;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Long> {

    // ---------------------------------------------
    // Find user by username
    // Buscar usuario por nombre de usuario
    // ---------------------------------------------
    Optional<UsuariosEntity> findByNombreUsuario(String nombreUsuario);

    // ---------------------------------------------
    // Find user by email
    // Buscar usuario por correo electrónico
    // ---------------------------------------------
    Optional<UsuariosEntity> findByEmail(String email);

    // ---------------------------------------------
    // Find all active users
    // Buscar todos los usuarios activos
    // ---------------------------------------------
    List<UsuariosEntity> findByActivoTrue();

    // ---------------------------------------------
    // Find all inactive users
    // Buscar todos los usuarios inactivos
    // ---------------------------------------------
    List<UsuariosEntity> findByActivoFalse();
}