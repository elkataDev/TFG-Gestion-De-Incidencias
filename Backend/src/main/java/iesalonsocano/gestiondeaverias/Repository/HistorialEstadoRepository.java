package iesalonsocano.gestiondeaverias.Repository;

import iesalonsocano.gestiondeaverias.entity.HistorialEstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HistorialEstadoRepository extends JpaRepository<HistorialEstadoEntity, Long> {
    List<HistorialEstadoEntity> findByIncidenciaIdOrderByFechaCambioAsc(Long incidenciaId);

    /**
     * Busca historial por incidencia con usuario precargado (JOIN FETCH).
     */
    @Query("SELECT h FROM HistorialEstadoEntity h LEFT JOIN FETCH h.usuario WHERE h.incidencia.id = :incidenciaId ORDER BY h.fechaCambio ASC")
    List<HistorialEstadoEntity> findByIncidenciaIdWithUsuario(@Param("incidenciaId") Long incidenciaId);
}
