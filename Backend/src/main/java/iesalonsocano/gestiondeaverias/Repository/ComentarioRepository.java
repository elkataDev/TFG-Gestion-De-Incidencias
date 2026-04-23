package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {
    List<ComentarioEntity> findByIncidenciaIdOrderByFechaAsc(Long incidenciaId);

    /**
     * Busca comentarios por incidencia con usuario precargado (JOIN FETCH).
     */
    @Query("SELECT c FROM ComentarioEntity c LEFT JOIN FETCH c.usuario WHERE c.incidencia.id = :incidenciaId ORDER BY c.fecha ASC")
    List<ComentarioEntity> findByIncidenciaIdWithUsuario(@Param("incidenciaId") Long incidenciaId);

    void deleteByIncidenciaId(Long incidenciaId);

    /** Borra todos los comentarios de una lista de incidencias (al eliminar usuario). */
    void deleteByIncidenciaIdIn(List<Long> incidenciaIds);

    /** Borra todos los comentarios hechos por un usuario (en incidencias ajenas). */
    void deleteByUsuarioId(Long usuarioId);
}
