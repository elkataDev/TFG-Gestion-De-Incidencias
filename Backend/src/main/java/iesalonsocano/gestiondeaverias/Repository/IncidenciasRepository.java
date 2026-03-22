package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity.EstadoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la gestión de persistencia de incidencias.
 * <p>
 * Proporciona métodos de consulta personalizados para filtrar incidencias
 * por estado, usuario o aula.
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see IncidenciasEntity
 */
@Repository
public interface IncidenciasRepository extends JpaRepository<IncidenciasEntity, Long> {

    /**
     * Busca todas las incidencias que tienen un estado específico.
     *
     * @param estado estado de la incidencia a filtrar
     * @return lista de incidencias con el estado especificado
     */
    List<IncidenciasEntity> findByEstado(EstadoIncidencia estado);

    /**
     * Busca todas las incidencias reportadas por un usuario específico.
     *
     * @param usuarioId identificador del usuario
     * @return lista de incidencias del usuario
     */
    List<IncidenciasEntity> findByUsuarioId(Long usuarioId);

    /**
     * Busca todas las incidencias asociadas a un aula específica.
     *
     * @param aulaId identificador del aula
     * @return lista de incidencias del aula
     */
    List<IncidenciasEntity> findByAulaId(Long aulaId);

    /**
     * Busca incidencias que tengan uno de los estados especificados.
     * <p>
     * Útil para filtrar incidencias abiertas (EN_PROGRESO, EN_ESPERA, etc.)
     * </p>
     *
     * @param estados lista de estados a buscar
     * @return lista de incidencias con alguno de los estados especificados
     */
    List<IncidenciasEntity> findByEstadoIn(List<EstadoIncidencia> estados);


    @Query("""
        SELECT i FROM IncidenciasEntity i
        WHERE (:estado IS NULL OR i.estado = :estado)
          AND (:categoria IS NULL OR i.categoria = :categoria)
          AND (:nombreAula IS NULL OR i.aula.nombre = :nombreAula)
    """)
    List<IncidenciasEntity> filtrarPorParametros(
            @Param("estado") IncidenciasEntity.EstadoIncidencia estado,
            @Param("categoria") IncidenciasEntity.CategoriaIncidencia categoria,
            @Param("nombreAula") String nombreAula
    );
}


