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
    @Query("SELECT i FROM IncidenciasEntity i LEFT JOIN FETCH i.aula LEFT JOIN FETCH i.usuario LEFT JOIN FETCH i.tecnicoAsignado LEFT JOIN FETCH i.activo WHERE i.estado = :estado")
    List<IncidenciasEntity> findByEstado(@Param("estado") EstadoIncidencia estado);

    /**
     * Obtiene todas las incidencias con aula y usuario precargados (JOIN FETCH).
     * Evita LazyInitializationException al acceder a las relaciones.
     *
     * @return lista de incidencias con relaciones cargadas
     */
    @Query("SELECT i FROM IncidenciasEntity i LEFT JOIN FETCH i.aula LEFT JOIN FETCH i.usuario LEFT JOIN FETCH i.tecnicoAsignado LEFT JOIN FETCH i.activo")
    List<IncidenciasEntity> findAllWithRelations();

    /**
     * Obtiene una incidencia por ID con aula y usuario precargados.
     *
     * @param id identificador de la incidencia
     * @return incidencia con relaciones cargadas
     */
    @Query("SELECT i FROM IncidenciasEntity i LEFT JOIN FETCH i.aula LEFT JOIN FETCH i.usuario LEFT JOIN FETCH i.tecnicoAsignado LEFT JOIN FETCH i.activo WHERE i.id = :id")
    java.util.Optional<IncidenciasEntity> findByIdWithRelations(@Param("id") Long id);

    /**
     * Busca incidencias por usuario con relaciones precargadas.
     *
     * @param usuarioId identificador del usuario
     * @return lista de incidencias del usuario con relaciones cargadas
     */
    @Query("SELECT i FROM IncidenciasEntity i LEFT JOIN FETCH i.aula LEFT JOIN FETCH i.usuario LEFT JOIN FETCH i.tecnicoAsignado LEFT JOIN FETCH i.activo WHERE i.usuario.id = :usuarioId")
    List<IncidenciasEntity> findByUsuarioIdWithRelations(@Param("usuarioId") Long usuarioId);

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
    @Query("SELECT i FROM IncidenciasEntity i LEFT JOIN FETCH i.aula LEFT JOIN FETCH i.usuario LEFT JOIN FETCH i.tecnicoAsignado LEFT JOIN FETCH i.activo WHERE i.aula.id = :aulaId")
    List<IncidenciasEntity> findByAulaId(@Param("aulaId") Long aulaId);

    /**
     * Busca incidencias que tengan uno de los estados especificados.
     * <p>
     * Útil para filtrar incidencias abiertas (EN_PROGRESO, EN_ESPERA, etc.)
     * </p>
     *
     * @param estados lista de estados a buscar
     * @return lista de incidencias con alguno de los estados especificados
     */
    @Query("SELECT i FROM IncidenciasEntity i LEFT JOIN FETCH i.aula LEFT JOIN FETCH i.usuario LEFT JOIN FETCH i.tecnicoAsignado LEFT JOIN FETCH i.activo WHERE i.estado IN :estados")
    List<IncidenciasEntity> findByEstadoIn(@Param("estados") List<EstadoIncidencia> estados);


    @Query("""
        SELECT i FROM IncidenciasEntity i
        LEFT JOIN FETCH i.aula
        LEFT JOIN FETCH i.usuario
        LEFT JOIN FETCH i.tecnicoAsignado
        LEFT JOIN FETCH i.activo
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
