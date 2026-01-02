package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity;
import iesalonsocano.gestiondeaverias.entity.IncidenciasEntity.EstadoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciasRepository extends JpaRepository<IncidenciasEntity, Long> {

    // ---------------------------------------------
    // Find incidents by status
    // Buscar incidencias por estado
    // ---------------------------------------------
    List<IncidenciasEntity> findByEstado(EstadoIncidencia estado);

    // ---------------------------------------------
    // Find incidents by user ID
    // Buscar incidencias por ID del usuario
    // ---------------------------------------------
    List<IncidenciasEntity> findByUsuarioId(Long usuarioId);

    // ---------------------------------------------
    // Find incidents by classroom ID
    // Buscar incidencias por ID del aula
    // ---------------------------------------------
    List<IncidenciasEntity> findByAulaId(Long aulaId);

    // ---------------------------------------------
    // Find open incidents (in progress or pending)
    // Buscar incidencias abiertas (en curso o en espera)
    // ---------------------------------------------
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


