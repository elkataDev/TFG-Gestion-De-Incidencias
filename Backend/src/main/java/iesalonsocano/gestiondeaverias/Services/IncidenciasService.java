package iesalonsocano.gestiondeaverias.Services;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
     * Busca incidencias por su estado.
     *
     * @param estado estado de la incidencia
     * @return lista de incidencias con el estado especificado
     */
    List<IncidenciasEntity> findByEstado(IncidenciasEntity.EstadoIncidencia estado);

    /**
     * Busca todas las incidencias reportadas por un usuario.
     *
     * @param usuarioId identificador del usuario
     * @return lista de incidencias del usuario
     */
    List<IncidenciasEntity> findByUsuarioId(Long usuarioId);

    /**
     * Busca todas las incidencias de un aula específica.
     *
     * @param aulaId identificador del aula
     * @return lista de incidencias del aula
     */
    List<IncidenciasEntity> findByAulaId(Long aulaId);

    List<IncidenciasEntity> filtrar(
            IncidenciasEntity.EstadoIncidencia estado,
            IncidenciasEntity.CategoriaIncidencia categoria,
            String nombreAula
    );




}