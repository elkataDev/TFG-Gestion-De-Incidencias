package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repositorio JPA para la gestión de persistencia de usuarios.
 * <p>
 * Proporciona métodos de consulta para buscar usuarios por nombre de usuario,
 * email o estado de activación.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see UsuariosEntity
 */
@Repository
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     * <p>
     * Utilizado principalmente para autenticación y login.
     * </p>
     *
     * @param nombreUsuario nombre de usuario a buscar
     * @return Optional con el usuario si existe, vacío si no se encuentra
     */
    Optional<UsuariosEntity> findByNombreUsuario(String nombreUsuario);

    /**
     * Busca un usuario por su correo electrónico.
     * <p>
     * Útil para validar unicidad o recuperación de contraseña.
     * </p>
     *
     * @param email correo electrónico del usuario
     * @return Optional con el usuario si existe, vacío si no se encuentra
     */
    Optional<UsuariosEntity> findByEmail(String email);

    /**
     * Busca todos los usuarios activos en el sistema.
     * <p>
     * Los usuarios activos son aquellos que pueden acceder al sistema.
     * </p>
     *
     * @return lista de usuarios con activo = true
     */
    List<UsuariosEntity> findByActivoTrue();

    /**
     * Busca todos los usuarios inactivos.
     * <p>
     * Útil para auditoría o gestión de cuentas deshabilitadas.
     * </p>
     *
     * @return lista de usuarios con activo = false
     */
    List<UsuariosEntity> findByActivoFalse();
}