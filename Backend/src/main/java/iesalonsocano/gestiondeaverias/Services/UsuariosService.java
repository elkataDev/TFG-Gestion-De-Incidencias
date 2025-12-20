package iesalonsocano.gestiondeaverias.services;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import java.util.List;
import java.util.Optional;

/**
 * Define el contrato para las operaciones de Usuarios.
 * Defines the contract for User operations.
 */
public interface UsuariosService {

    /**
     * Obtiene todos los usuarios.
     * Gets all users.
     */
    List<UsuariosEntity> findAll();

    /**
     * Busca un usuario por su ID.
     * Finds a user by their ID.
     */
    Optional<UsuariosEntity> findById(Long id);

    /**
     * Guarda (crea o actualiza) un usuario.
     * Saves (creates or updates) a user.
     */
    UsuariosEntity save(UsuariosEntity usuario);

    /**
     * Elimina un usuario por su ID.
     * Deletes a user by their ID.
     */
    void deleteById(Long id);

    /**
     * Busca usuario por nombre de usuario.
     * Finds user by username.
     */
    Optional<UsuariosEntity> findByNombreUsuario(String nombreUsuario);

    /**
     * Busca usuario por email.
     * Finds user by email.
     */
    Optional<UsuariosEntity> findByEmail(String email);

    /**
     * Busca todos los usuarios activos.
     * Finds all active users.
     */
    List<UsuariosEntity> findByActivoTrue();
}