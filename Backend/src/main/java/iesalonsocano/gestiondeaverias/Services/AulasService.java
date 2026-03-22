package iesalonsocano.gestiondeaverias.Services;

import iesalonsocano.gestiondeaverias.entity.AulasEntity;
import java.util.List;
import java.util.Optional;

/**
 * Define el contrato (las operaciones) para la gestión de Aulas.
 * Defines the contract (operations) for Classroom management.
 */
public interface AulasService {

    /**
     * Obtiene todas las aulas.
     * Gets all classrooms.
     */
    List<AulasEntity> findAll();

    /**
     * Busca un aula por su ID.
     * Finds a classroom by its ID.
     */
    Optional<AulasEntity> findById(Long id);

    /**
     * Guarda (crea o actualiza) un aula.
     * Saves (creates or updates) a classroom.
     */
    AulasEntity save(AulasEntity aula);

    /**
     * Elimina un aula por su ID.
     * Deletes a classroom by its ID.
     */
    void deleteById(Long id);

    /**
     * Busca un aula por su nombre.
     * <p>
     * Útil para validar unicidad y búsquedas por nombre.
     * </p>
     *
     * @param nombre nombre del aula a buscar
     * @return Optional con el aula si existe
     */
    Optional<AulasEntity> findByNombre(String nombre);
}