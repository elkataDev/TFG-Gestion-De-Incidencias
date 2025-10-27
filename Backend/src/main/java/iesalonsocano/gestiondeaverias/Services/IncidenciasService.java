package iesalonsocano.gestiondeaverias.service;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import java.util.List;
import java.util.Optional;

/**
 * Define el contrato para las operaciones de Incidencias.
 * Defines the contract for Incident operations.
 */
public interface IncidenciasService {

    /**
     * Obtiene todas las incidencias.
     * Gets all incidents.
     */
    List<IncidenciasEntity> findAll();

    /**
     * Busca una incidencia por su ID.
     * Finds an incident by its ID.
     */
    Optional<IncidenciasEntity> findById(Long id);

    /**
     * Guarda (crea o actualiza) una incidencia.
     * Saves (creates or updates) an incident.
     */
    IncidenciasEntity save(IncidenciasEntity incidencia);

    /**
     * Elimina una incidencia por su ID.
     * Deletes an incident by its ID.
     */
    void deleteById(Long id);

    /**
     * Busca incidencias por estado.
     * Finds incidents by status.
     */
    List<IncidenciasEntity> findByEstado(IncidenciasEntity.EstadoIncidencia estado);

    /**
     * Busca incidencias por ID de usuario.
     * Finds incidents by user ID.
     */
    List<IncidenciasEntity> findByUsuarioId(Long usuarioId);

    /**
     * Busca incidencias por ID de aula.
     * Finds incidents by classroom ID.
     */
    List<IncidenciasEntity> findByAulaId(Long aulaId);
}