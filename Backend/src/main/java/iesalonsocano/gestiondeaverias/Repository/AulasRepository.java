package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la gestión de persistencia de aulas.
 * <p>
 * Extiende JpaRepository proporcionando métodos CRUD automáticos y
 * consultas personalizadas para la entidad AulasEntity.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see AulasEntity
 */
@Repository
public interface AulasRepository extends JpaRepository<AulasEntity, Long> {

    /**
     * Busca un aula por su nombre.
     *
     * @param nombre nombre del aula a buscar
     * @return Optional con el aula si existe, vacío si no se encuentra
     */
    Optional<AulasEntity> findByNombre(String nombre);

    /**
     * Verifica si ya existe un aula con el nombre especificado.
     * <p>
     * Útil para validar la unicidad antes de crear o actualizar.
     * </p>
     *
     * @param nombre nombre del aula a verificar
     * @return true si existe un aula con ese nombre, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}
