package iesalonsocano.gestiondeaverias.Services;

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
     * Busca un usuario por su nombre de usuario.
     * <p>
     * Usado principalmente para autenticación.
     * </p>
     *
     * @param nombreUsuario nombre de usuario
     * @return Optional con el usuario si existe
     */
    Optional<UsuariosEntity> findByNombreUsuario(String nombreUsuario);

    /**
     * Busca un usuario por su email.
     *
     * @param email correo electrónico
     * @return Optional con el usuario si existe
     */
    Optional<UsuariosEntity> findByEmail(String email);

    /**
     * Busca todos los usuarios activos del sistema.
     *
     * @return lista de usuarios con activo = true
     */
    List<UsuariosEntity> findByActivoTrue();
}